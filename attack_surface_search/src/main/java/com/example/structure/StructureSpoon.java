package com.example.structure;

import com.example.auxiliary_functions.Functions;
import com.example.input_structure.InputStructureMethod;
import com.example.structure.graph.CpgGraph;
import com.example.structure.graph.Edge;
import com.example.structure.graph.Label;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class StructureSpoon {
    private Path source;
    private CtModel model;
    private CpgGraph graph;
    // Карта: полное имя интерфейса → список реализующих классов
    private Map<CtInterface<?>, List<CtClass<?>>> interfaceToClasses = new HashMap<>();
    // Карта: полная сигнатура метода интерфейса → список реализаций
    private Map<CtMethod<?>, List<CtMethod<?>>> implementationMap = new HashMap<>();

    // Глобальный счетчик для генерации уникальных ID узлов в DOT-файле
    private Map<String, CtType<?>> allTypesMap = new HashMap<>();
    public StructureSpoon(Path source) {
        this.source = source;
        this.model = null;
        this.graph = new CpgGraph();
    }
    public CtModel getModel() {
        return model;
    }

    public CpgGraph getGraph() {
        return graph;
    }

    public CtModel initSpoon() throws IOException {
        Launcher launcher = new Launcher();

        // Указываем путь к исходникам вашего проекта
        launcher.addInputResource(source.toRealPath().toString());

        // Отключаем режим classpath для анализа без полной компиляции
        launcher.getEnvironment().setNoClasspath(true);

        // Строим модель
        this.model = launcher.buildModel();

        return this.model;
    }

    /**
     * Шаг 1: Строим карту "интерфейс → классы, которые его реализуют"
     */
    private void buildInterfaceToClassesMap() {
        for (CtType<?> type : model.getAllTypes()) {
            if (type instanceof CtInterface) {
                List<CtClass<?>> implementations = new ArrayList<>();

                // Ищем все классы, реализующие этот интерфейс
                for (CtType<?> candidate : model.getAllTypes()) {
                    if (candidate instanceof CtClass<?> &&
                            !candidate.isAbstract() &&
                            candidate.isSubtypeOf(type.getReference())) {
                        implementations.add((CtClass<?>) candidate);
                    }
                }

                interfaceToClasses.put((CtInterface<?>) type, implementations);
            }
        }
    }
    /**
     * Шаг 2: Для каждого метода интерфейса находим реализации в классах
     */
    private void buildImplementationMap() {
        for (Map.Entry<CtInterface<?>, List<CtClass<?>>> entry : interfaceToClasses.entrySet()) {
            CtInterface<?> ctInterface = entry.getKey();
            List<CtClass<?>> implementingClasses = entry.getValue();

            // Для каждого метода интерфейса
            for (CtMethod<?> interfaceMethod : ctInterface.getMethods()) {
                List<CtMethod<?>> implementations = new ArrayList<>();
                implementationMap.put(interfaceMethod, new ArrayList<>());

                // Ищем реализацию в каждом классе, который implements наш интерфейс
                for (CtClass<?> clazz : implementingClasses) {
                    // для каждого метода внутри имплементирующего класса
                    for (CtMethod<?> methodImpl : clazz.getAllMethods()) {
//                        boolean isCorrectMethod = true;
                        if (// Проверяем имя
                            methodImpl.getSimpleName().equals(interfaceMethod.getSimpleName()) &&
                            // Проверяем количество параметров
                            methodImpl.getParameters().size() == interfaceMethod.getParameters().size()
                        ) {
                                implementations.add(methodImpl);
                        }
                    }
                }
                implementationMap.get(interfaceMethod).addAll(implementations);
            }
        }
    }



    public void initCpgGraph() {
        // 1. Заполняем карту всех типов
        allTypesMap.clear();
        for (CtType<?> type : model.getAllTypes()) {
            allTypesMap.put(type.getQualifiedName(), type);
        }

        // 2. для всех интерфейсов ищем связки интерфес - его реализации
        interfaceToClasses.clear();
        buildInterfaceToClassesMap();
        buildImplementationMap();

        // 3. Строим CPG граф в памяти
        this.graph = buildCpgFromSource();

    }


    // ==================== ПОСТРОЕНИЕ ГРАФА ====================

    public CpgGraph buildCpgFromSource() {
        Functions functions = new Functions();

        // ПЕРВЫЙ ПРОХОД: Создаем узлы для всех типов и их методов
        addAllNodesToCpgGraph();

        // ВТОРОЙ ПРОХОД: Создаем узлы для всех типов и их методов
        for (CtExecutable<?> node : graph.getNodes()) {
            // добавляем все вызовы конструкторов из ноды
            // ищем CtConstructorCall
            for (Edge edge : functions.findAllConstructorsCalls(node)) {
                graph.addEdge(edge);
            }
            // Ищем все вызовы методов из ноды
            for (Edge edge : functions.getAllMethodCallsEdges(node)) {
                if (isMethodOfInterface(edge.getTo())) {
                    CtMethod<?> interfaceMethod = (CtMethod<?>) edge.getTo();
                    if (tryToResolveInterfaceMethod(edge)) {
                        System.out.println("разрешили метод интерфейса!!" +
                                "\nвместо :: " + Functions.getFullSignatureForMethod(interfaceMethod) +
                                "\nбудет :: " + Functions.getFullSignatureForMethod((CtMethod<?>) edge.getTo())
                        );
                    }
                    continue;
                }
                graph.addEdge(edge);
            }
        }

        return graph;
    }

    public boolean isMethodOfInterface(CtExecutable<?> executable) {
        if (executable instanceof CtMethod<?>) {
            // 1. Получаем класс или интерфейс, содержащий executable
            CtType<?> declaringType = ((CtMethod<?>)executable).getDeclaringType();

            // 2. Проверяем, является ли этот тип интерфейсом
            return declaringType instanceof CtInterface;
        }
        return false;
    }

    private boolean tryToResolveInterfaceMethod(Edge edge) {
        // 1. Получаем все инструкции тела метода
        List<CtStatement> allStatements = new ArrayList<>();
        if (edge.getFrom() instanceof CtMethod<?>) {
            allStatements.addAll(((CtMethod<?>) edge.getFrom()).getBody().getStatements());
        } else {
            System.out.println("Странный метод интерфейса! не является методом!!!");
            return false;
        }
        CtInvocation<?> invocation = (CtInvocation<?>) edge.getCallExpression();
        // Получаем целевой объект (то, слева от точки)
        CtExpression<?> target = invocation.getTarget();

        System.out.println("Целевой объект вызова: " + target);

        // Анализируем, кто вызывал
        if (target == null) {
            // варианты:
            // - вызов конструктора - у интерфейса нет конструкторов
            // - метода этого же класса без явного вызова this - я вызываю метод интерфейса, но я класс -> значит что это не может быть вызов моего метода
            // + Вызов статического метода через явный импорт - если я интерфейс -> там есть явная реализация static method -> её и берём, всё супер
            // + вызов статического метода через вызов класса - если я интерфейс -> там есть явная реализация static method -> её и берём, всё супер
            System.out.println("  → Вызов статического метода, нужно взять его реализацию");
            // TODO добавить методы интерфейсов с реализацией в nodes графа
            return true;
        }
        else if (target instanceof CtThisAccess) {
            // варианты:
            // - явный вызов метода от this  - я вызываю метод интерфейса, но я класс -> значит что это не может быть вызов моего метода
            System.out.println("  → Вызов через this (текущий объект) - такого не может быть!!!");
        }
        else if (target instanceof CtSuperAccess) {
            // варианты:
            // - вызов метода родительского класса - работает только для классов, а я метод интерфейса - не может быть
            System.out.println("  → Вызов через super (родительский класс) - такого не может быть!!!");
        }
        else if (target instanceof CtFieldRead) {
            // варианты:
            // + Поле класса - смогу определить реализацию ТОЛЬКО при явно присваивании в ЭТОМ же методе
            // + Статическое поле - смогу определить реализацию ТОЛЬКО при явно присваивании в ЭТОМ же методе
            CtFieldRead<?> fieldRead = (CtFieldRead<?>) target;
            System.out.println("  → Вызов через поле: " + fieldRead.getVariable().getSimpleName());
            // рекурсия с последующей обработкой после выхода
        }
        else if (target instanceof CtVariableRead) {
            // варианты:
            // + Локальная переменная - смогу определить реализацию ТОЛЬКО при явно присваивании в ЭТОМ же методе
            // + Параметр метода - не смогу определить реализацию
            // + Элемент массива - не смогу определить реализацию
            // учтено ранее : + Поле класса - смогу определить реализацию ТОЛЬКО при явно присваивании в ЭТОМ же методе
            // учтено ранее : + Статическое поле - смогу определить реализацию ТОЛЬКО при явно присваивании в ЭТОМ же методе
            CtVariableRead<?> varRead = (CtVariableRead<?>) target;
            System.out.println("  → Вызов через переменную: " + varRead.getVariable().getSimpleName());
            // рекурсия с последующей обработкой после выхода
        }
        else if (target instanceof CtInvocation) {
            // варианты:
            // + вызов метода (класса/интерфейса) TODO чо делать?
            System.out.println("  → Вызов через результат другого вызова (цепочка): " + target);
            // рекурсия с последующей обработкой после выхода
        }
        else {
            // варианты:
            // литералы ("string".method()) и прочая дичь TODO - чо делать??
            System.out.println("  → Другой тип: " + target.getClass().getSimpleName());
        }
        return false;
    }


    private void addAllNodesToCpgGraph() {
        List<Map<String, String>> signaturesList = new ArrayList<>();
        for (CtType<?> type : allTypesMap.values()) {
            // Методы интерфейсов - пропускаем
            if (type instanceof CtInterface) {
                continue;
            }
            // Методы всех классов
            for (CtMethod<?> method : type.getMethods()) {
                graph.addNode(method);
                // Сохраняем сигнатуру метода
                Map<String, String> methodInfo = new LinkedHashMap<>();
                methodInfo.put("signature", method.getDeclaringType().getQualifiedName() + "#" + method.getSignature());
                signaturesList.add(methodInfo);
            }

            // Конструкторы классов
            if (type instanceof CtClass<?>) {
                for (CtConstructor<?> constructor : ((CtClass<?>) type).getConstructors()) {
                    graph.addNode(constructor);
                    Map<String, String> methodInfo = new LinkedHashMap<>();
                    methodInfo.put("signature", constructor.getDeclaringType().getQualifiedName() + "#" + constructor.getSignature());
                    signaturesList.add(methodInfo);
                }
            }
            // Конструкторы енамов
            if (type instanceof CtEnum<?>) {
                for (CtConstructor<?> constructor : ((CtEnum<?>)type).getConstructors()) {
                    graph.addNode(constructor);
                    Map<String, String> methodInfo = new LinkedHashMap<>();
                    methodInfo.put("signature", constructor.getDeclaringType().getQualifiedName() + "#" + constructor.getSignature());
                    signaturesList.add(methodInfo);
                }
            }
            // TODO обработка вызовов методов сторонних библиотек
        }
        writeSignaturesToJsonFile(signaturesList);
    }

    // Запись сигнатур в JSON файл
    private void writeSignaturesToJsonFile(List<Map<String, String>> signaturesList) {
// Извлекаем сигнатуры
        List<String> signatures = new ArrayList<>();
        for (Map<String, String> item : signaturesList) {
            signatures.add(item.get("signature"));
        }

        // Ручное построение JSON с переносами строк
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < signatures.size(); i++) {
            json.append("  \"").append(escapeJson(signatures.get(i))).append("\"");
            if (i < signatures.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("]");

        // Записываем в файл
        try (FileWriter writer = new FileWriter("methods_signatures.json")) {
            writer.write(json.toString());

        } catch (IOException e) {
            System.err.println("❌ Ошибка при записи JSON файла: " + e.getMessage());
        }
    }

    // Вспомогательный метод для экранирования спецсимволов
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public Map<CtMethod<?>, List<CtMethod<?>>> getImplementationMap() {
        return implementationMap;
    }
}


package com.example.structure;

import com.example.structure.auxiliary_functions.Functions;
import com.example.structure.graph.CpgGraph;
import com.example.structure.graph.Edge;
import com.example.structure.graph.Label;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;

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
//    private static int nodeCounter = 0;
//    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
//                System.out.println("нашёл интерфейс, ищу методы");
//                String interfaceName = type.getQualifiedName();
                List<CtClass<?>> implementations = new ArrayList<>();

                // Ищем все классы, реализующие этот интерфейс
                for (CtType<?> candidate : model.getAllTypes()) {
//                    System.out.println("смотрю кондидатов методы");
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
//                String methodSignature = getFullMethodSignature(interfaceMethod);
                List<CtMethod<?>> implementations = new ArrayList<>();
                implementationMap.put(interfaceMethod, new ArrayList<>());

                // Ищем реализацию в каждом классе, который implements наш интерфейс
                for (CtClass<?> clazz : implementingClasses) {
                    // для каждого метода внутри имплементирующего класса
                    for (CtMethod<?> methodImpl : clazz.getAllMethods()) {
                        boolean isCorrectMethod = true;
                        if (interfaceMethod.getSimpleName().equals("setProgress")) {
                            System.out.println("may impl setProgress: " + methodImpl.getSimpleName());
                        }
                        if (// Проверяем имя
                            methodImpl.getSimpleName().equals(interfaceMethod.getSimpleName()) &&
                            // Проверяем количество параметров
                            methodImpl.getParameters().size() == interfaceMethod.getParameters().size()
                        ) {
//                            for (int i = 0; i < methodImpl.getParameters().size(); ++i) {
//                                if (methodImpl.getParameters().get(i).getType() != interfaceMethod.getParameters().get(i).getType()) {
//                                    isCorrectMethod = false;
//                                    break;
//                                }
//                            }
//                            if (isCorrectMethod)
                                implementations.add(methodImpl);
//                            if (interfaceMethod.getSimpleName().equals("setProgress")) {
//                                System.out.println("real impl setProgress: " + methodImpl.getSimpleName());
//                                System.out.println(implementations);
//                            }
                        }
                    }
                }
//                System.out.println("для интерфемного метода " + interfaceMethod);
//                System.out.println("найдены реализации " + implementations);
                implementationMap.get(interfaceMethod).addAll(implementations);
                if (interfaceMethod.getSimpleName().equals("setProgress")) {
                    System.out.println(implementationMap.get(interfaceMethod));
                }
            }
        }
//        System.out.println(implementationMap);
    }



    public void initCpgGraph() throws Exception {

        // 2. Заполняем карту всех типов
        allTypesMap.clear();
        for (CtType<?> type : model.getAllTypes()) {
            allTypesMap.put(type.getQualifiedName(), type);
        }

        // ищем связки интерфес - его реализации
        interfaceToClasses.clear();
        buildInterfaceToClassesMap();
        buildImplementationMap();

        // 3. Строим CPG граф в памяти
        this.graph = buildCpgFromSource();

//        StartMethod startMethod = JsonToClassGenerator.createMethodStructureFromJson("project_structure/method.json");
//        startMethod.print();

//        CtExecutable<?> startCtMethod = getMethodByStructure(startMethod);
//        if (startCtMethod instanceof CtMethod<?>) {
////            System.out.println("\n===========================\nстартовый метод");
////            System.out.println("сигнатура = " + startCtMethod.getSignature() + "\n===========================\n");
//            graph.setStart((CtMethod<?>) startCtMethod);
//            analyseFlowFromStartingPoint(startCtMethod);
//        } else {
//            System.out.println("Не найден метод после getMethodByStructure: !!!!!!!!!!!!!");
//        }

//        graph.printStats();
    }

    public CtExecutable<?> getMethodByStructure(StartMethod startMethod) {
        List<CtExecutable<?>> nodes = graph.getNodes();
        Functions functions = new Functions();
        for (CtExecutable<?> node : nodes) {
            if (
                    node instanceof CtMethod<?> &&
                    functions.checkIsMethodMatchesStructure((CtMethod<?>) node, startMethod)
            ) {
//                System.out.println("Найден метод: " + node.getSignature());
                return node;
            } else if (
                    node instanceof CtConstructor<?> &&
                    functions.checkIsConstructorMatchesStructure((CtConstructor<?>) node, startMethod)
            ) {
//                    System.out.println("Найден конструктор: " + node.getSignature());
                    return node;
            }
//            else {
//                System.out.println("Не тот метод: !!!!!!!!!!!!!");
//            }
        }
//        System.out.println("Не найден метод: !!!!!!!!!!!!!");
        return null;
    }


    // ==================== ПОСТРОЕНИЕ ГРАФА ====================

    public CpgGraph buildCpgFromSource() {
        Functions functions = new Functions();

        // ПЕРВЫЙ ПРОХОД: Создаем узлы для всех типов и их методов
        addAllNodesToCpgGraph();

        for (CtExecutable<?> node : graph.getNodes()) {
            // добавляем все вызовы конструкторов из ноды
            // ищем CtConstructorCall
            for (Edge edge : functions.findAllConstructorsCalls(node)) {
                graph.addEdge(edge);
            }
            // Ищем все вызовы методов из ноды
            for (Edge edge : functions.getAllMethodCallsEdges(node)) {
                graph.addEdge(edge);
            }

//            // Ищем все вызовы методов интерфейсов из ноды
//            for (Edge edge : functions.getAllInterfaceMethodCallsEdges(node, implementationMap)) {
//                graph.addEdge(edge);
//            }
        }

        return graph;
    }


    private void addAllNodesToCpgGraph() {
        for (CtType<?> type : allTypesMap.values()) {
            // Методы интерфейсов - пропускаем
            if (type instanceof CtInterface) {
                continue;
            }
            // Методы всех классов
            for (CtMethod<?> method : type.getMethods()) {
                graph.addNode(method);
            }

            // Конструкторы классов
            if (type instanceof CtClass<?>) {
                for (CtConstructor<?> constructor : ((CtClass<?>) type).getConstructors()) {
                    graph.addNode(constructor);
                }
//                // ====== ПОЛЯ КЛАССА С ТИПОМ-ИНТЕРФЕЙСОМ ======
//                CtClass<?> ctClass = (CtClass<?>) type;
//                for (CtField<?> field : ctClass.getFields()) {
//                    CtTypeReference<?> fieldType = field.getType();
//
//                    // Проверяем, является ли тип поля интерфейсом
//                    if (isInterface(fieldType)) {
//                        System.out.println("Найдено поле с типом-интерфейсом:");
//                        System.out.println("  Класс: " + ctClass.getQualifiedName());
//                        System.out.println("  Поле: " + field.getSimpleName());
//                        System.out.println("  Тип: " + fieldType.getQualifiedName());
//                        System.out.println("  Интерфейс: ДА");
//
//                        // Добавляем узел поля в граф
////                        graph.addNode(field);
//                    }
//                }
            }
            // Конструкторы енамов
            if (type instanceof CtEnum<?>) {
                for (CtConstructor<?> constructor : ((CtEnum<?>)type).getConstructors()) {
                    graph.addNode(constructor);
                }
            }
        }
    }

//    // Вспомогательный метод для проверки, является ли тип интерфейсом
//    private static boolean isInterface(CtTypeReference<?> typeRef) {
//        if (typeRef == null) return false;
//
//        CtType<?> typeDeclaration = typeRef.getTypeDeclaration();
//        return typeDeclaration instanceof CtInterface;
//    }

    private void analyseFlowFromStartingPoint(CtExecutable<?> startMethod) {
//        System.out.println("\n=================\nищу реализации интерфейсов в " + startMethod.getSimpleName());
        // ищу всех соседей стартового метода: все методы, куда были переходы из стартового
        List<Edge> neighbors = new ArrayList<>();
        for (Edge edge : graph.getEdges()) {
            if (edge.getFrom() == startMethod) {
                neighbors.add(edge);
            }
        }

        // для каждого соседа узнаю: был ли это вызов метода интерфейса или нет?
        for (Edge edge : neighbors) {
            // Получаем цель вызова (то, что стоит перед точкой)
            CtExpression<?> expression = edge.getCallExpression();
            if (expression instanceof CtConstructorCall<?>) {

            } else if (expression instanceof CtInvocation<?>) {
                CtInvocation<?> invocation = (CtInvocation<?>) expression;
                CtExpression<?> target = invocation.getTarget();

                if (target != null) {
                    // Это может быть переменная
                    if (target instanceof CtVariableRead) {
                        CtVariableRead<?> varRead = (CtVariableRead<?>) target;
                        CtVariable<?> variable = varRead.getVariable().getDeclaration();
                        String varName = variable.getSimpleName();
                        String varType = variable.getType().getQualifiedName();

//                        System.out.println("Метод вызван на переменной: " + varName);
//                        System.out.println("  Тип переменной: " + varType);

                        // Если тип — интерфейс, то мы нашли то, что нужно!
//                        if (variable.getType().getTypeDeclaration() instanceof CtInterface) {
//                            System.out.println("  Тип является ИНТЕРФЕЙСОМ!");
//                        }
                    }
                    // Или поле класса
                    else if (target instanceof CtFieldRead) {
                        CtFieldRead<?> fieldRead = (CtFieldRead<?>) target;
//                        System.out.println("Метод вызван на поле: " +
//                                fieldRead.getVariable().getSimpleName());
                    }
                    // Или результат другого вызова
                    else if (target instanceof CtInvocation) {
                        CtInvocation<?> nestedInv = (CtInvocation<?>) target;
//                        System.out.println("Метод вызван на результате вызова: " +
//                                nestedInv.getExecutable().getSimpleName() + "()");
                    }
//                    // Или this
//                    else if (target instanceof CtThisAccess) {
//                        System.out.println("Метод вызван на this");
//                    }
                }
            }

            if (edge.getLabel() == Label.KNOWN) {
//                System.out.println("\n==================\nперешла в " + edge.getTo().getSignature());
                analyseFlowFromStartingPoint(edge.getTo());
            }
        }

//        for (Edge edge : neighbors) {
//            if (edge.getTo() instanceof CtMethod<?>)
//                analyseFlowFromStartingPoint((CtMethod<?>) edge.getTo());
//        }

//        Queue<CtExecutable<?>> methodQueue = new ArrayDeque<>();
//        methodQueue.add(startMethod);
//
//        while (!methodQueue.isEmpty()) {
//            // извлекает и удаляет первый в очереди
//            CtExecutable<?> curMethod = methodQueue.poll();
//            // TODO сделать поведение не как список, а как дерево
//
//            for (Edge e : graph.getEdges()) {
//                if (e.getFrom() == curMethod) {
//                    // TODO найти какие аргументы туда попали
////                    for(CtParameter param : e.to.getParameters())
////                    if (e.to.getDeclaringType() instanceof CtInterface<?>) {
////                        // TODO найти имплементацию метода
////                        // TODO варианты: интерфейс, метод аннотации, сторонняя либа
////                    }
//                    if (e.getLabel() == Label.KNOWN) {
//                        methodQueue.add(e.getTo());
//                    }
//                }
//            }
//        }
    }

    public Map<CtMethod<?>, List<CtMethod<?>>> getImplementationMap() {
        return implementationMap;
    }
}

package org.example.structure;

import org.example.generators.JsonToClassGenerator;
import org.example.structure.graph.*;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class StructureSpoon {
    private Path source;
    private CtModel model;
    private CpgGraph graph;
    // Глобальный счетчик для генерации уникальных ID узлов в DOT-файле
    private Map<String, CtType<?>> allTypesMap = new HashMap<>();
//    private static int nodeCounter = 0;
//    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public StructureSpoon(Path source) {
        this.source = source;
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

    public void initCpgGraph() throws Exception {

        // 2. Заполняем карту всех типов
        allTypesMap.clear();
        for (CtType<?> type : model.getAllTypes()) {
            allTypesMap.put(type.getQualifiedName(), type);
        }

        // 3. Строим CPG граф в памяти
        this.graph = buildCpgFromSource();

        StartMethod startMethod = JsonToClassGenerator.createMethodStructureFromJson("project_structure/method.json");
//        startMethod.print();

//        CtExecutable<?> startCtMethod = getMethodByStructure(startMethod);
//        if (startCtMethod instanceof CtMethod<?>) {
//            analyseFlowFromStartingPoint((CtMethod<?>) startCtMethod);
//        } else if (startCtMethod != null) {
//            System.out.println("start (main) не является методом!!!");
//        } else {
//            System.out.println("Не найден метод после getMethodByStructure: !!!!!!!!!!!!!");
//        }

//        graph.printStats();
    }

    public CtExecutable<?> getMethodByStructure(StartMethod startMethod) {
        List<CtExecutable<?>> nodes = graph.getNodes();
//        System.out.println("Ищем метод в нодах");
        for (CtExecutable<?> node : nodes) {
//            System.out.println("Сейчас смотрим на ноду " + node.getSimpleName());
//            System.out.println("Сравниваю с " + startMethod.getMethodName());
            if (node instanceof CtMethod<?> &&
                    node.getSimpleName().equals(startMethod.getMethodName()) &&
                    node.getParameters().size() == startMethod.getMethodArguments().size() &&
                    ((CtMethod<?>) node).getDeclaringType().getSimpleName().equals(startMethod.getClassName())) {
                System.out.println("Найден метод: " + node.getSignature());
                return node;
            } else if (node instanceof CtConstructor<?>) {
                if (((CtConstructor<?>) node).getDeclaringType().getSimpleName().equals(startMethod.getMethodName()) &&
                    node.getParameters().size() == startMethod.getMethodArguments().size() &&
                    ((CtConstructor<?>) node).getDeclaringType().getSimpleName().equals(startMethod.getClassName())) {
                    System.out.println("Найден конструктор: " + node.getSignature());
                    return node;
                }
            } else {
//                System.out.println("Найден странный метод: " + node.getClass());
            }
        }
        System.out.println("Не найден метод: !!!!!!!!!!!!!");
        return null;
    }

    // ==================== ПОСТРОЕНИЕ ГРАФА ====================

    public CpgGraph buildCpgFromSource() {
        CpgGraph graph = new CpgGraph();

        // ПЕРВЫЙ ПРОХОД: Создаем узлы для всех типов и их методов
        for (CtType<?> type : model.getAllTypes()) {
            // Создаем узлы методов
            for (CtMethod<?> method : type.getMethods()) {
                graph.addNode(method);
            }

            // Конструкторы классов
            if (type instanceof CtClass<?>) {
                for (CtConstructor<?> constructor : ((CtClass<?>) type).getConstructors()) {
                    graph.addNode(constructor);
//                    if (type.getSimpleName().equals("Game"))
//                        System.out.println("Нашли Game !!!!!!!!!!!!");
                }
            }
            // Конструкторы енамов
            if (type instanceof CtEnum<?>) {
                for (CtConstructor<?> constructor : ((CtEnum<?>)type).getConstructors()) {
                    graph.addNode(constructor);
                }
            }
        }

        for (CtExecutable<?> node : graph.getNodes()) {
            // добавляем все вызовы конструкторов из ноды
            // ищем CtConstructorCall
            List<CtConstructorCall<?>> ctConstructorCalls = node.getElements(
                    new TypeFilter<>(CtConstructorCall.class));

            for (CtConstructorCall<?> constructorCall : ctConstructorCalls) {
                CtExecutableReference<?> executableRef = constructorCall.getExecutable();
                if (executableRef == null) continue;
                // Пытаемся получить все вызываемые CtConstructor
                CtExecutable<?> execDeclaration = executableRef.getExecutableDeclaration();

                if (execDeclaration instanceof CtConstructor<?>) {
                    CtConstructor<?> calledConstructor = (CtConstructor<?>) execDeclaration;

//                    if (!graph.getNodes().contains(calledConstructor)) {
//                        System.out.println("этого метода нет в нодах! ::: " + calledConstructor.getSignature());
//                    }

                    graph.addEdge(
                            node,
                            calledConstructor,
                            RelationType.CALLS,
                            constructorCall,
                            Label.KNOWN,
                            Condition.ALWAYS
                    );
                } else if (execDeclaration == null) {
                    // Метод не разрешен — используем информацию из референса

                    graph.addEdge(
                            node,
                            null,
                            RelationType.CALLS,
                            constructorCall,
                            Label.UNKNOWN,
                            Condition.ALWAYS
                    );
                }
            }
            // Ищем все вызовы методов из ноды
            List<CtInvocation<?>> invocations = node.getElements(
                    new TypeFilter<>(CtInvocation.class));

            for (CtInvocation<?> invocation : invocations) {
                CtExecutableReference<?> executableRef = invocation.getExecutable();
                if (executableRef == null) continue;
                // Пытаемся получить CtMethod
                CtExecutable<?> execDeclaration = executableRef.getExecutableDeclaration();

                if (execDeclaration instanceof CtMethod) {
                    CtMethod<?> calledMethod = (CtMethod<?>) execDeclaration;

//                    if (!graph.getNodes().contains(calledMethod)) {
//                        System.out.println("этого метода нет в нодах! ::: " + calledMethod.getSignature());
//                    }

                    graph.addEdge(
                            node,
                            calledMethod,
                            RelationType.CALLS,
                            invocation,
                            Label.KNOWN,
                            Condition.ALWAYS
                    );
                } else if (execDeclaration == null) {
                    // Метод не разрешен — используем информацию из референса
                    String methodName = executableRef.getSimpleName();
                    String ownerType = executableRef.getDeclaringType() != null ?
                            executableRef.getDeclaringType().getQualifiedName() : "Unknown";

                    graph.addEdge(
                            node,
                            null,
                            RelationType.CALLS,
                            invocation,
                            Label.UNKNOWN,
                            Condition.ALWAYS
                    );
                }
            }
        }

        return graph;
    }

    private void analyseFlowFromStartingPoint(CtMethod<?> startMethod) {
        graph.setStart(startMethod);

        Queue<CtExecutable<?>> methodQueue = new ArrayDeque<>();
        methodQueue.add(startMethod);

        while (!methodQueue.isEmpty()) {
            // извлекает и удаляет первый в очереди
            CtExecutable<?> curMethod = methodQueue.poll();
            // TODO сделать поведение не как список, а как дерево

            for (Edge e : graph.getEdges()) {
                if (e.getFrom() == curMethod) {
                    // TODO найти какие аргументы туда попали
//                    for(CtParameter param : e.to.getParameters())
//                    if (e.to.getDeclaringType() instanceof CtInterface<?>) {
//                        // TODO найти имплементацию метода
//                        // TODO варианты: интерфейс, метод аннотации, сторонняя либа
//                    }
                    if (e.getLabel() == Label.KNOWN) {
                        methodQueue.add(e.getTo());
                    }
                }
            }
        }
    }
}

//public class OriginalSourceForArg {
//    RelationType type; // CONTAINS, CALLS
//    CtExpression<?> arg; // Аргументы вызова (для CALLS) в формате JSON массива
//
//
//    public OriginalSourceForArg(CtExpression<?> arg) {
//        this.arg = arg;
//    }
//
//    public CtExpression<?> getArg() {
//        return this.arg;
//    }
//}
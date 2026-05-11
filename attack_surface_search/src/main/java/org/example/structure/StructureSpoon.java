package org.example.structure;

import org.example.generators.JsonToClassGenerator;
import org.example.structure.auxiliary_functions.Functions;
import org.example.structure.graph.*;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtConstructorCall;
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

    public void initCpgGraph() throws Exception {

        // 2. Заполняем карту всех типов
        allTypesMap.clear();
        for (CtType<?> type : model.getAllTypes()) {
            allTypesMap.put(type.getQualifiedName(), type);
        }

        // 3. Строим CPG граф в памяти
        this.graph = buildCpgFromSource();

//        StartMethod startMethod = JsonToClassGenerator.createMethodStructureFromJson("project_structure/method.json");
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
        Functions functions = new Functions();
        for (CtExecutable<?> node : nodes) {
            if (
                    node instanceof CtMethod<?> &&
                    functions.checkIsMethodMatchesStructure((CtMethod<?>) node, startMethod)
            ) {
                System.out.println("Найден метод: " + node.getSignature());
                return node;
            } else if (
                    node instanceof CtConstructor<?> &&
                    functions.checkIsConstructorMatchesStructure((CtConstructor<?>) node, startMethod)
            ) {
                    System.out.println("Найден конструктор: " + node.getSignature());
                    return node;
            } else {
                System.out.println("Не тот метод: !!!!!!!!!!!!!");
            }
        }
        System.out.println("Не найден метод: !!!!!!!!!!!!!");
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
        }

        return graph;
    }


    private void addAllNodesToCpgGraph() {
        for (CtType<?> type : model.getAllTypes()) {
            // Методы всех классов
            for (CtMethod<?> method : type.getMethods()) {
                graph.addNode(method);
            }

            // Конструкторы классов
            if (type instanceof CtClass<?>) {
                for (CtConstructor<?> constructor : ((CtClass<?>) type).getConstructors()) {
                    graph.addNode(constructor);
                }
            }
            // Конструкторы енамов
            if (type instanceof CtEnum<?>) {
                for (CtConstructor<?> constructor : ((CtEnum<?>)type).getConstructors()) {
                    graph.addNode(constructor);
                }
            }
        }
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

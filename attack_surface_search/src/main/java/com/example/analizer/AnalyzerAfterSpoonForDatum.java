package com.example.analizer;

import com.example.analizer.followed_data.FollowedDatum;
import com.example.generators.JsonToClassGenerator;
import com.example.structure.StructureSpoon;
import com.example.analizer.followed_data.location.FollowedDatumLocation;
import com.example.analizer.followed_data.location.MethodArgumentLocation;
import com.example.result_structure.ResultEdge;
import com.example.result_structure.ResultGraph;
import com.example.structure.graph.Edge;
import com.example.structure.StartMethod;
import com.example.structure.StartMethodArgument;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;

import java.util.*;

public class AnalyzerAfterSpoonForDatum {
//    private CtModel model;
    private final FollowedDatum startFollowedDatum;
    private StructureSpoon structureSpoonMy;
    public AnalyzerAfterSpoonForDatum(String inputDatumJson) throws Exception {
        this.startFollowedDatum = JsonToClassGenerator.createDatumFromLocationJson(inputDatumJson);
    }
    public ResultGraph analyzeDatumAndGetResult(StructureSpoon structureSpoon) {
        this.structureSpoonMy = structureSpoon;
        ResultGraph resultGraph = new ResultGraph(
                startFollowedDatum
        );

//        ArrayList<FollowedDatum> results = new ArrayList<>();
//        results.add(startFollowedDatum);
        Queue<FollowedDatum> queueForNextClass = new ArrayDeque<>();
        queueForNextClass.add(startFollowedDatum);
        Set<FollowedDatum> isVisited = new HashSet<>();
//        ResultNode curResult = resultNode;
//        curResult.addOperation(new ResultStructureNode(
//                startFollowedDatum.getLocation()
//        ));

        while (!queueForNextClass.isEmpty()) {
            // извлекает и удаляет первый в очереди
            FollowedDatum curDatum = queueForNextClass.poll();
//            ResultNode fromNode = new ResultNode(
//                    curDatum.getLocation()
//            );
//            if (!resultGraph.isContainsNode(fromNode))
//                resultGraph.addNode(fromNode);
            // TODO сделать поведение не как список, а как дерево

            // получаем детей для bfs
            ArrayList<FollowedDatum> newData = oneStepOfAnalyzingDatum(curDatum, structureSpoon);
//            results.addAll(newData);
            if (!newData.isEmpty()) {
                for (FollowedDatum datum : newData) {
                    if (!contains(isVisited, datum)) {
                        queueForNextClass.add(datum);
                        resultGraph.addEdge(
                                new ResultEdge(curDatum, datum)
                        );
                    }
//                    ResultNode toNode = new ResultNode(
//                            datum.getLocation()
//                    );
//
//                    if (!resultGraph.isContainsNode(toNode)) {
//                        resultGraph.addNode(toNode);
//                    }
//                    ResultEdge edge = new ResultEdge(fromNode, toNode);
//                    if (!resultGraph.isContainsEdge(edge))
//                        resultGraph.addEdge(edge);
                }
            } else {
//                System.out.println("no new data !!!!");
            }
            isVisited.add(curDatum);
//            System.out.println("отладка" + isVisited.size());
            resultGraph.addNode(curDatum);
        }

//        int i = 0;
//        for (FollowedDatum result : results) {
//            result.getLocation().print(i);
//            ++i;
//        }
//        System.out.println("====================== result ==================");
//        resultGraph.print();
        return resultGraph;
    }

    private boolean contains(Set<FollowedDatum> isVisited, FollowedDatum requiredDatum) {
//        System.out.println("1234567890");
        for (FollowedDatum datum : isVisited) {
//            datum.getLocation().print(1);
//            requiredDatum.getLocation().print(2);
            if (datum.isEquals(requiredDatum)) {
                return true;
            }
        }
        return false;
    }

    // анализируем метод и конструктор, все операции в нём
    private ArrayList<FollowedDatum> oneStepOfAnalyzingDatum(FollowedDatum followedDatum, StructureSpoon structureSpoon) {
        ArrayList<FollowedDatum> newData = new ArrayList<>();
        FollowedDatumLocation rowLocation = followedDatum.getLocation();
        if (rowLocation instanceof MethodArgumentLocation) {
            MethodArgumentLocation location = (MethodArgumentLocation) rowLocation;
            StartMethod methodSource = new StartMethod(
                    location.getJavaPackage(),
                    location.getJavaClass(),
                    location.getMethod().getName(),
                    Collections.nCopies(location.getMethod().getNumberOfArguments(), new StartMethodArgument())
            );

            CtExecutable<?> executable = structureSpoon.getMethodByStructure(methodSource);

//            System.out.println("закончили поиск executable");
            if (executable != null) {
//                System.out.println("закончили поиск executable не null");
                if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
//                    System.out.println("закончили поиск executable = метод или конструктор");
                    newData.addAll(analiseOperation(location, executable, structureSpoon));
                }
            }
        }
        return newData;
    }

//    for all startData
//    analyzeOneStartDatum
//    BFS: while not all children analyzed get datum and analyze it
//    analyzeOneDatum
//    - assignment
//    - newVariable
//    - invocation
//    - constructor
//    - dirty

    private List<FollowedDatum> analiseOperation(MethodArgumentLocation location, CtExecutable<?> executable, StructureSpoon structureSpoon) {
        List<FollowedDatum> newData = new ArrayList<>();
        String datumName = executable.getParameters().get(location.getPositionInMethod()).getSimpleName();
//        System.out.println("!!!!! ищем аргумент ::: номер = " + location.getPositionInMethod() + " имя = " + datumName);
//        System.out.println("Ищем нужный вызов");
        for (Edge edge : structureSpoon.getGraph().getEdges()) {
            if (edge.getFrom() == executable && edge.getTo() != null) {
                CtExpression<?> callExpression = edge.getCallExpression();
                if (callExpression instanceof CtInvocation<?>) {
                    newData.addAll(analyzeInvocationEdge(datumName, edge));
                } else if (callExpression instanceof CtConstructorCall<?>) {
//                    System.out.println("это переход в конструктор!!!!");
                    newData.addAll(analyzeConstructorCallEdge(datumName, edge));
                }
            }
        }
        return newData;
    }

    private List<FollowedDatum> analyzeInvocationEdge(String datumName, Edge edge) {
        List<FollowedDatum> newData = new ArrayList<>();
        if (!(edge.getCallExpression() instanceof CtInvocation<?>)) {
            System.out.println("ошибочный вызов метода analyzeInvocationEdge");
            return newData;
        }

        int argInd = 0;
        CtMethod<?> methodTo = (CtMethod<?>) edge.getTo();
        CtInvocation<?> ctInvocation = (CtInvocation<?>) edge.getCallExpression();
        for (CtExpression<?> arg : ctInvocation.getArguments()) {
            // Если это переменная - получить её имя
            if (arg instanceof CtVariableRead) {
                String argName = ((CtVariableRead<?>) arg).getVariable().getSimpleName();
//                System.out.println("нашли invocation с переменной " + argName);
                if (argName.equals(datumName)) {
                    if (methodTo.getDeclaringType() instanceof CtInterface<?>) {
                        System.out.println("datum ушла в метод интерфейса " + methodTo.getSimpleName());
                        System.out.println(structureSpoonMy.getImplementationMap().get(methodTo));

                        for (CtMethod<?> methodImpl : structureSpoonMy.getImplementationMap().get(methodTo)) {
                            newData.add(new FollowedDatum(
                                    new MethodArgumentLocation(
                                            methodImpl.getDeclaringType().getPackage().getQualifiedName(),
                                            methodImpl.getDeclaringType().getSimpleName(),
                                            new Method(methodImpl.getSimpleName(), methodImpl.getParameters().size()),
                                            argInd
                                    )
                            ));
                            newData.get(newData.size() - 1).getLocation().print(0);
                        }
                    } else {
//                        System.out.println("datum ушла в метод " + methodTo.getSimpleName());
                        newData.add(new FollowedDatum(
                                new MethodArgumentLocation(
                                        methodTo.getDeclaringType().getPackage().getQualifiedName(),
                                        methodTo.getDeclaringType().getSimpleName(),
                                        new Method(methodTo.getSimpleName(), methodTo.getParameters().size()),
                                        argInd
                                )
                        ));
                    }
                }
            }
            ++argInd;
        }
        return newData;
    }

    private List<FollowedDatum> analyzeConstructorCallEdge(String datumName, Edge edge) {
        List<FollowedDatum> newData = new ArrayList<>();
        if (!(edge.getCallExpression() instanceof CtConstructorCall<?>)) {
            System.out.println("ошибочный вызов метода analyzeConstructorCallEdge");
            return newData;
        }

        int argInd = 0;
        CtConstructor<?> constructorTo = (CtConstructor<?>) edge.getTo();
        CtConstructorCall<?> ctConstructorCall = (CtConstructorCall<?>) edge.getCallExpression();
        for (CtExpression<?> arg : ctConstructorCall.getArguments()) {
            // Если это переменная - получить её имя
            if (arg instanceof CtVariableRead) {
                String argName = ((CtVariableRead<?>) arg).getVariable().getSimpleName();
                if (argName.equals(datumName)) {
//                    System.out.println("datum ушла в " + constructorTo.getDeclaringType().getSimpleName());
//                    System.out.println("аргументы ::: сравниваем " + datumName + " c " + argName + " номер аргумента = " + argInd);
                    newData.add(new FollowedDatum(
                            new MethodArgumentLocation(
                                    constructorTo.getDeclaringType().getPackage().getQualifiedName(),
                                    constructorTo.getDeclaringType().getSimpleName(),
                                    new Method(constructorTo.getDeclaringType().getSimpleName(), constructorTo.getParameters().size()),
                                    argInd
                            )
                    ));
                }
            }
            ++argInd;
        }
        return newData;
    }


//    private CtMethod<?> getRequiredMethod(StructureSpoon structureSpoon, String javaPackageName, String javaClassName, Method methodStructure) {
//        CpgGraph cpgGraph = structureSpoon.getGraph();
//        List<CtMethod<?>> nodes = cpgGraph.getNodes();
//        for (CtMethod<?> node : nodes) {
//            if (Objects.equals(node.getSimpleName(), methodStructure.getName()) &&
//                    node.getParameters().size() == methodStructure.getNumberOfArguments() &&
//                    Objects.equals(node.getDeclaringType().getSimpleName(), javaClassName)) {
//                System.out.println("Найден метод: " + node.getSignature());
//                return node;
//            }
//        }
//        return null;
//    }

//    старая версия
//    private CtMethod<?> getRequiredMethod(StructureSpoon structureSpoon, String javaPackageName, String javaClassName, Method methodStructure) {
//        List<CtPackage> packages = structureSpoon.getModel().getElements(new TypeFilter<>(CtPackage.class)).stream()
//                .filter(curPackage -> curPackage != null && javaPackageName.equals(curPackage.getQualifiedName()))
//                .toList();
//        if (packages.isEmpty()) {
//            System.out.println("Не найден класс");
//            return null;
//        } else if (packages.size() > 1) {
//            System.out.println("Класс не единственный - некорректное описание");
//            return null;
//        }
//        CtPackage javaPackage = packages.get(0);
//        List<CtClass<?>> classes = javaPackage.getTypes().stream()
//                .filter(type -> type instanceof CtClass) // Оставляем только классы (enum и интерфейсы отсеются)
//                .map(type -> (CtClass<?>) type)          // Приводим к CtClass
//                .filter(clazz -> javaClassName.equals(clazz.getSimpleName()))
//                .collect(Collectors.toList());
//        if (classes.isEmpty()) {
//            System.out.println("Не найден класс");
//            return null;
//        } else if (classes.size() > 1) {
//            System.out.println("Класс не единственный - некорректное описание");
//            return null;
//        }
//        CtClass<?> javaClass = classes.get(0);
//        List<CtMethod<?>> method = javaClass.getMethods().stream() // Используем getMethods()
//                .filter(m -> methodStructure.getName().equals(m.getSimpleName())) // Проверка имени
//                .filter(m -> m.getParameters().size() == methodStructure.getNumberOfArguments()) // Проверка кол-ва аргументов
//                .toList();
//        if (method.isEmpty()) {
//            System.out.println("Не найден метод");
//            return null;
//        } else if (method.size() > 1) {
//            System.out.println("Метод не единственный - некорректное описание");
//            return null;
//        }
//        System.out.println("Найден метод: " + method.get(0).getSignature());
//        return method.get(0);
//    }
    public FollowedDatum analyzeAction(CtVariableAccess<?> access) {
        CtElement oneWhoOperatesOnDatum = access.getParent();
        FollowedDatum newDatum = new FollowedDatum();

        if (access instanceof CtVariableWrite) {
            System.out.println("Изменение: Параметру присваивается новое значение: " + oneWhoOperatesOnDatum);
        } else if (access instanceof CtVariableRead) {
            // вызов обычного метода, не конструктора
            if (oneWhoOperatesOnDatum instanceof CtInvocation) {
                // Проверяем роль datum в этом вызове:
                // она вызывает метод, или её вызывают в методе ?
                if (access.getRoleInParent() == CtRole.ARGUMENT) {
                    // СЮДА попадут: process(datum), Math.max(datum, 10) и т.д.
                    System.out.println("Вызов: Параметр передан в метод " + ((CtInvocation<?>)oneWhoOperatesOnDatum).getExecutable().getSimpleName());
                    return ActionAnalyzer.analyzeActionCtInvocation((CtInvocation<?>) oneWhoOperatesOnDatum, (CtVariableRead<?>) access);
                }
                else if (access.getRoleInParent() == CtRole.TARGET) {
                    // СЮДА попадут: datum.method()
                    System.out.println("Вызов: Параметр вызывает метод " + ((CtInvocation<?>)oneWhoOperatesOnDatum).getExecutable().getSimpleName());
                    return null;
                }
            }
            // вызов конструктора
            else if (oneWhoOperatesOnDatum instanceof CtConstructorCall) {
                System.out.println("Вызов: Параметр передан в конструктор " + ((CtConstructorCall<?>)oneWhoOperatesOnDatum).getExecutable().getSimpleName());
                return ActionAnalyzer.analyzeActionCtConstructorCall((CtConstructorCall<?>) oneWhoOperatesOnDatum, (CtVariableRead<?>) access);
            }
            // присваивание в существующую переменную
//            else if (oneWhoOperatesOnDatum instanceof CtAssignment) {
//                System.out.println("Операция: Параметр участвует в присваивании в ранее созданную переменную: " + ((CtAssignment)oneWhoOperatesOnDatum).getShortRepresentation());
//                return ActionAnalyzer.analyzeActionCtAssignment((CtAssignment<?, ?>) oneWhoOperatesOnDatum, (CtVariableRead<?>) access);
//            }
//            // присваивание в создающуюся переменную
//            else if (oneWhoOperatesOnData instanceof CtLocalVariable) {
//                System.out.println("Операция: Параметр участвует в присваивании в создающуюся (локальную) переменную: " + oneWhoOperatesOnData.getShortRepresentation());
//                return analizeActionCtLocalVariable((CtLocalVariable<?>) oneWhoOperatesOnData, (CtVariableRead<?>) access);
//            }
//            // return datum
//            else if (oneWhoOperatesOnData instanceof CtReturn) {
//                System.out.println("Возврат: Параметр возвращается из метода");
//                return analizeActionCtReturn((CtReturn<?>) oneWhoOperatesOnData, (CtVariableRead<?>) access);
//            }
            // остальное
            else {
                System.out.println("Чтение: Параметр используется в: " + oneWhoOperatesOnDatum.getClass().getSimpleName());
            }
        } else {
            System.out.println("Неизвестный тип операции над параметром");
        }
        return null;
    }

}

package org.example.analizer;

import org.example.generators.JsonToClassGenerator;
import org.example.analizer.followed_data.FollowedDatum;
import org.example.analizer.followed_data.FollowedDatumLocation;
import org.example.analizer.followed_data.MethodArgumentLocation;
import org.example.result_structure.ResultEdge;
import org.example.result_structure.ResultGraph;
import org.example.result_structure.ResultNode;
import org.example.structure.graph.Edge;
import org.example.structure.StartMethod;
import org.example.structure.StartMethodArgument;
import org.example.structure.StructureSpoon;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;

import java.util.*;

public class AnalizerAfterSpoon {
//    private CtModel model;
    private FollowedDatum startFollowedDatum;
    public AnalizerAfterSpoon(String inputDatumJson) throws Exception {
        this.startFollowedDatum = JsonToClassGenerator.createDatumFromLocationJson(inputDatumJson);
    }
//    public void setModel(CtModel model) {
//        this.model = model;
//    }
    public ResultGraph analyze(StructureSpoon structureSpoon) {
        ResultGraph resultGraph = new ResultGraph(new ResultNode(
                    startFollowedDatum.getLocation()
        ));
        ArrayList<FollowedDatum> results = new ArrayList<>();
        results.add(startFollowedDatum);
        Queue<FollowedDatum> queueForNextClass = new ArrayDeque<>();
        queueForNextClass.add(startFollowedDatum);
//        ResultNode curResult = resultNode;
//        curResult.addOperation(new ResultStructureNode(
//                startFollowedDatum.getLocation()
//        ));

        while (!queueForNextClass.isEmpty()) {
            // извлекает и удаляет первый в очереди
            FollowedDatum curDatum = queueForNextClass.poll();
            ResultNode fromNode = new ResultNode(
                    curDatum.getLocation()
            );
            if (!resultGraph.isContainsNode(fromNode))
                resultGraph.addNode(fromNode);
            // TODO сделать поведение не как список, а как дерево

            ArrayList<FollowedDatum> newData = analiseDatum(curDatum, structureSpoon);
            results.addAll(newData);
            if (!newData.isEmpty()) {
                queueForNextClass.addAll(newData);
                for (FollowedDatum datum : newData) {
                    ResultNode toNode = new ResultNode(
                            datum.getLocation()
                    );

                    if (!resultGraph.isContainsNode(toNode)) {
                        resultGraph.addNode(toNode);
                    }
                    ResultEdge edge = new ResultEdge(fromNode, toNode);
                    if (!resultGraph.isContainsEdge(edge))
                        resultGraph.addEdge(edge);
                }
            } else {
                System.out.println("no new data !!!!");
            }
        }

        int i = 0;
        for (FollowedDatum result : results) {
            result.getLocation().print(i);
            ++i;
        }
        return resultGraph;
    }

    private ArrayList<FollowedDatum> analiseDatum(FollowedDatum followedDatum, StructureSpoon structureSpoon) {
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

            System.out.println("закончили поиск executable");
            if (executable != null) {
                System.out.println("закончили поиск executable не null");
                if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
                    System.out.println("закончили поиск executable = метод или конструктор");
                    newData.addAll(analiseOperation(location, executable, structureSpoon));
                }
            }
        }
        return newData;
    }

    private List<FollowedDatum> analiseOperation(MethodArgumentLocation location, CtExecutable<?> executable, StructureSpoon structureSpoon) {
        List<FollowedDatum> newData = new ArrayList<>();
        String datumName = executable.getParameters().get(location.getPositionInMethod()).getSimpleName();
        System.out.println("!!!!! ищем аргумент ::: номер = " + location.getPositionInMethod() + " имя = " + datumName);
        System.out.println("Ищем нужный вызов");
        for (Edge edge : structureSpoon.getGraph().getEdges()) {
//            System.out.println("нашли метод " + edge.getFrom().getSimpleName());
            if (edge.getFrom() == executable && edge.getTo() != null) {
//                System.out.println("нашли переход в " + edge.getTo().getSimpleName());
                CtExpression<?> callExpression = edge.getCallExpression();
                if (callExpression instanceof CtInvocation<?>) {
                    int argInd = 0;
                    CtMethod<?> methodTo = (CtMethod<?>) edge.getTo();
                    CtInvocation<?> ctInvocation = (CtInvocation<?>) callExpression;
                    for (CtExpression<?> arg : ctInvocation.getArguments()) {
                        // Если это переменная - получить её имя
                        if (arg instanceof CtVariableRead) {
                            String argName = ((CtVariableRead<?>) arg).getVariable().getSimpleName();
                            if (argName.equals(datumName)) {
                                System.out.println("datum ушла в " + methodTo.getSimpleName());
                                newData.add(new FollowedDatum(
                                        new MethodArgumentLocation(
                                                methodTo.getDeclaringType().getPackage().getQualifiedName(),
                                                methodTo.getDeclaringType().getSimpleName(),
                                                new Method(edge.getTo().getSimpleName(), edge.getTo().getParameters().size()),
                                                argInd
                                        )
                                ));
                            }
                        }
                        ++argInd;
                    }
                } else if (callExpression instanceof CtConstructorCall<?>) {
                    System.out.println("это переход в конструктор!!!!");
                    int argInd = 0;
                    CtConstructor<?> constructorTo = (CtConstructor<?>) edge.getTo();
                    CtConstructorCall<?> ctConstructorCall = (CtConstructorCall<?>) callExpression;
                    for (CtExpression<?> arg : ctConstructorCall.getArguments()) {
                        // Если это переменная - получить её имя
                        if (arg instanceof CtVariableRead) {
                            String argName = ((CtVariableRead<?>) arg).getVariable().getSimpleName();
                            if (argName.equals(datumName)) {
                                System.out.println("datum ушла в " + constructorTo.getDeclaringType().getSimpleName());
                                System.out.println("аргументы ::: сравниваем " + datumName + " c " + argName + " номер аргумента = " + argInd);
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
                }
            }
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
            else if (oneWhoOperatesOnDatum instanceof CtAssignment) {
                System.out.println("Операция: Параметр участвует в присваивании в ранее созданную переменную: " + ((CtAssignment)oneWhoOperatesOnDatum).getShortRepresentation());
                return ActionAnalyzer.analyzeActionCtAssignment((CtAssignment<?, ?>) oneWhoOperatesOnDatum, (CtVariableRead<?>) access);
            }
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

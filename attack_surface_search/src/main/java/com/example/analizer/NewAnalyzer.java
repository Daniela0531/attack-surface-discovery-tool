package com.example.analizer;

//import com.example.analizer.followed_data.ExpressionInMethod;
import com.example.analizer.followed_data.*;
import com.example.analizer.followed_data.operation.AssignmentInMethod;
import com.example.auxiliary_functions.Functions;
import com.example.generators.JsonToClassGenerator;
import com.example.result_structure.ResultEdge;
import com.example.result_structure.ResultGraph;
import com.example.structure.StructureSpoon;
import com.example.structure.graph.Edge;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

public class NewAnalyzer {
    //    private CtModel model;
    private FollowedDatum startFollowedDatum;
    private StructureSpoon structureSpoon;
    public NewAnalyzer(StructureSpoon structureSpoon) {
        this.structureSpoon = structureSpoon;
    }

    public ResultGraph analyzeDatumAndGetResult(FollowedDatum followedDatum) {
        this.startFollowedDatum = followedDatum;
        ResultGraph resultGraph = new ResultGraph(
                startFollowedDatum
        );
        Queue<FollowedDatum> queueForNextClass = new ArrayDeque<>();
        queueForNextClass.add(startFollowedDatum);
        Set<FollowedDatum> isVisited = new HashSet<>();

        while (!queueForNextClass.isEmpty()) {
            // извлекает и удаляет первый в очереди
            FollowedDatum curDatum = queueForNextClass.poll();
//            System.out.println(curDatum.getName());

            // получаем детей для bfs
            ArrayList<FollowedDatum> newData = getAllChildrenByDatum(curDatum);
            if (!newData.isEmpty()) {
                for (FollowedDatum datum : newData) {
                    if (!Functions.contains(isVisited, datum)) {
                        queueForNextClass.add(datum);
                        resultGraph.addEdge(
                                new ResultEdge(curDatum, datum)
                        );
                    }
                }
            }
            isVisited.add(curDatum);
            resultGraph.addNode(curDatum);
            if (curDatum instanceof ClassField) {
                System.out.println("curDatum :: " + curDatum.getName());
            }
        }
        return resultGraph;
    }

    // анализируем метод и конструктор, все операции в нём
    private ArrayList<FollowedDatum> getAllChildrenByDatum(FollowedDatum followedDatum) {
        ArrayList<FollowedDatum> newData = new ArrayList<>();
//        FollowedDatumLocation rowLocation = followedDatum.getLocation();
        if (followedDatum instanceof FollowedDatumInMethodContext) {
            newData.addAll(analyzingMethodContext((FollowedDatumInMethodContext) followedDatum));
        } else if (followedDatum instanceof ClassField) {
            // TODO анализ полей класса
            return newData;
        } else {
            System.out.println("неизвестный тип FollowedDatum");
        }
        return newData;
    }


    private ArrayList<FollowedDatum> analyzingMethodContext(FollowedDatumInMethodContext followedDatum) {
        ArrayList<FollowedDatum> newData = new ArrayList<>();

        // methodLocation должна знать, в каком она методе
        if (followedDatum instanceof MethodArgument) {
            CtExecutable<?> parentMethod = followedDatum.getLocation();
            // получить список всех операций
            // для каждой операции провести анализ, создать newData
            // если datum утекает в другую переменную anotherDatum:
            // анализ начиная с заданного statement переменной anotherDatum - рекурсия
            analyseAllOperations(parentMethod, null, followedDatum, newData);
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

    private void analyseAllOperations(CtExecutable<?> parentMethod, CtStatement ctStatement, FollowedDatum followedDatum, List<FollowedDatum> newData) {
        // получить список всех операций
//        System.out.println("analyseAllOperations :: " + followedDatum.getName());
        List<CtStatement> statements = findStatementOperationsOnDatumInMethod(parentMethod, ctStatement, followedDatum.getName());
//        List<FollowedDatum> newData = new ArrayList<>();
        String datumName = followedDatum.getName();
        for (CtStatement statement : statements) {
//            System.out.println("DEBUG: " + "instanceof CtFieldWrite: " + (statement instanceof CtFieldWrite));
            if (statement instanceof CtInvocation<?>) {
                // создать новую datum
                List<Edge> edges = structureSpoon.getGraph().getEdgesByExpression((CtExpression<?>) statement);
                for (Edge edge : edges) {
                    newData.addAll(getNewDatumFromInvocation(datumName, edge));
                }
            } else if (statement instanceof CtConstructorCall<?>) {
                // создать новую datum
                List<Edge> edges = structureSpoon.getGraph().getEdgesByExpression((CtExpression<?>) statement);
                for (Edge edge : edges) {
                    newData.addAll(getNewDatumFromConstructorCall(datumName, edge));
                }
            }
            else if (statement instanceof CtLocalVariable<?>) {
                System.out.println("CtLocalVariable :: " + ((CtLocalVariable<?>) statement).getSimpleName());
                CtExpression<?> defaultValue = ((CtLocalVariable<?>) statement).getDefaultExpression();
                if (defaultValue == null) {
                    System.out.println("Странная инициализация локальной переменной");
                    continue;
                }
                // создать новую datum
                FollowedDatum newDatum = new LocalVariableInMethod((CtLocalVariable<?>) statement);
                System.out.println("CtLocalVariable :: создать новую datum " + newDatum.getName());
                newData.add(newDatum);
                // запуск рекурсии
                analyseAllOperations(parentMethod, statement, newDatum, newData);
            }
            else if (statement instanceof CtFieldWrite) {
                System.out.println("CtFieldWrite");
                // запись в поле
                // создать новую datum
                FollowedDatum newDatum = new ClassField(((CtFieldWrite<?>) statement).getVariable().getDeclaration());
                newData.add(newDatum);
                // TODO анализ вне метода
                // analyseAllOperations(parentMethod, statement, newDatum, newData);
            }
            else if (statement!=null && statement instanceof CtAssignment<?, ?>) {
                // создать новую datum
//                System.out.println("CtAssignment " + statement.prettyprint());
                CtExpression<?> assignment = ((CtAssignment<?, ?>) statement).getAssignment();
                CtExpression<?> assigned = ((CtAssignment<?, ?>) statement).getAssigned();
                if (
                        assignment instanceof CtVariableRead<?> &&
                        ((CtVariableRead<?>) assignment).getVariable().getSimpleName().equals(datumName)) {
                    if (assigned instanceof CtVariableWrite<?>) {
                        FollowedDatum newDatum = new AssignmentInMethod((CtVariableWrite<?>) assigned);
                        newData.add(newDatum);
                        // запуск рекурсии
                        analyseAllOperations(parentMethod, statement, newDatum, newData);
                    } else if (assigned instanceof CtFieldRead) {
                        System.out.println("CtFieldWrite");
                        FollowedDatum newDatum = new ClassField(((CtFieldWrite<?>) statement).getVariable().getDeclaration());
                        newData.add(newDatum);
                    }
                }
            }
            else {
//                System.out.println("Неизвестный способ использования datum ::: ");
//                System.out.println(statement);
            }
        }
    }

//    private Edge getEdgeFromInvocation(CtInvocation<?> statement) {
//        return null;
//    }

    // возвращает список операций начиная с заданного statement, в которых участвует datum
    private List<CtStatement> findStatementOperationsOnDatumInMethod(CtExecutable<?> parentMethod, CtStatement requiredStatement, String datumName) {
        List<CtStatement> foundOperations = new ArrayList<>();

        // 2. Получаем все инструкции тела метода
        if (parentMethod.getBody() != null) {
            List<CtStatement> allStatements = parentMethod.getBody().getElements(new TypeFilter<>(CtStatement.class));
            boolean statementFound = false;
            if (requiredStatement == null) {
                statementFound = true;
            }

            // 3. Проходим по всем инструкциям
            for (CtStatement statement : allStatements) {
                // Начинаем собирать после того, как нашли наш invocation
                if (statementFound) {
                    // Проверяем, используется ли datumName в этой инструкции
                    if (usesVariable(statement, datumName)) {
                        foundOperations.add(statement);
                    }
                }
                // Проверяем, является ли текущая инструкция нашим invocation'ом
                if (statement == requiredStatement) {
                    statementFound = true;
                }
            }
        }

        return foundOperations;
    }

    // TODO реализовать обработку блоков
    // возвращает список операций начиная с заданного statement, в которых участвует datum
    private List<CtStatement> findStatementAfterRequiredConsideringBlocksInMethod(CtExecutable<?> parentMethod, CtStatement requiredStatement) {
        List<CtStatement> foundStatements = new ArrayList<>();

        // 2. Получаем все инструкции тела метода
//        List<CtStatement> allStatements = parentMethod.getBody().getElements(new TypeFilter<>(CtStatement.class));
        List<CtStatement> allStatements = parentMethod.getBody().getStatements();
        // если передан null -> нам нужно всё тело метода
        if (requiredStatement == null) {
            return allStatements;
        }
        boolean statementFound = false;
        int curDepth = 0;
        int targetDepth = -1;

        // 3. Проходим по всем инструкциям
        for (CtStatement statement : allStatements) {
            if (Functions.isStructuralBlock(statement)) {
                ++curDepth;
            }
            // если вышли за пределы целевого блока
            if (curDepth < targetDepth)
                return foundStatements;
            // Начинаем собирать после того, как нашли наш invocation
            if (statementFound) {
                foundStatements.add(statement);
            }
            // Проверяем, является ли текущая инструкция нашим invocation'ом
            if (statement == requiredStatement) {
                statementFound = true;
                targetDepth = curDepth;
            }
        }

        return foundStatements;
    }

    // parentBlockStatement {
    //   statement              cur = 0 target = 0
    //   if () {                cur = 0 target = 0
    //     statement            cur = 1 target = 1
    //     block {              cur = 1 target = 1
    //       requiredStatement  cur = 2 target = 2
    //       {
    //         statement        cur = 3 target = 3
    //       }
    //       statement          cur = 2 target = 2
    //     }
    //     statement            cur = 1 target = 2
    //   }
    //   statement              cur = 0 target = 2
    // }
    // TODO для обработки блоков
    private int findChildStatementsAfterRequiredConsideringBlocks(
            CtStatement parentBlockStatement,
            CtStatement requiredStatement,
            List<CtStatement> isVisited,
            int curDepth,
            List<CtStatement> foundStatements) {
        List<CtStatement> allStatement = parentBlockStatement.getElements(new TypeFilter<>(CtStatement.class));
        if (requiredStatement == null) {
            foundStatements.addAll(allStatement);
            return curDepth;
        }
//        List<CtStatement> foundStatements = new ArrayList<>();
        boolean statementFound = false;
        int newTargetDepth = curDepth;

        // 3. Проходим по всем инструкциям
        for (CtStatement statement : allStatement) {
            if (isVisited.contains(statement)) {
                continue;
            }
            isVisited.add(statement);
            if (Functions.isStructuralBlock(statement)) {
                ++curDepth;
                if (statementFound) {
                    int targetDepth = findChildStatementsAfterRequiredConsideringBlocks(
                            statement,
                            null,
                            isVisited,
                            curDepth,
                            foundStatements
                    );
                    if (curDepth < targetDepth)
                        return targetDepth;
                } else {
                    int targetDepth = findChildStatementsAfterRequiredConsideringBlocks(
                            statement,
                            requiredStatement,
                            isVisited,
                            curDepth,
                            foundStatements
                    );
                    if (curDepth < targetDepth)
                        return targetDepth;
                }
                --curDepth;
            }
//            // если вышли за пределы целевого блока
//            if (curDepth < targetDepth)
//                return targetDepth;
            // Начинаем собирать после того, как нашли наш invocation
            if (statementFound) {
                foundStatements.add(statement);
            }
            // Проверяем, является ли текущая инструкция нашим invocation'ом
            if (statement == requiredStatement) {
                statementFound = true;
                newTargetDepth = curDepth;
            }
        }

        return newTargetDepth;
    }

    // Вспомогательный метод для проверки использования переменной в инструкции
    private boolean usesVariable(CtStatement statement, String varName) {
        // Используем Filter для поиска всех чтений переменной с заданным именем
        List<CtVariableRead<?>> varReads = statement.getElements(
                new TypeFilter<>(CtVariableRead.class)
        );

        for (CtVariableRead<?> read : varReads) {
            if (varName.equals(read.getVariable().getSimpleName())) {
                return true;
            }
        }
        return false;
    }

//    private List<FollowedDatum> analiseOperationsInMethodForParameter(MethodLocation location, CtExecutable<?> executable, StructureSpoon structureSpoon) {
//        List<FollowedDatum> newData = new ArrayList<>();
//        String datumName = executable.getParameters().get(location.getPositionInMethod()).getSimpleName();
//        for (Edge edge : structureSpoon.getGraph().getEdges()) {
//            if (edge.getFrom() == executable && edge.getTo() != null) {
//                CtExpression<?> callExpression = edge.getCallExpression();
//                if (callExpression instanceof CtInvocation<?>) {
//                    newData.addAll(analyzeInvocationEdge(datumName, edge));
//                } else if (callExpression instanceof CtConstructorCall<?>) {
//                    newData.addAll(analyzeConstructorCallEdge(datumName, edge));
//                }
//            }
//        }
//        return newData;
//    }


    private List<FollowedDatum> getNewDatumFromInvocation(String datumName, Edge edge) {
        List<FollowedDatum> newData = new ArrayList<>();
        if (!(edge.getCallExpression() instanceof CtInvocation<?>)) {
            System.out.println("ошибочный вызов метода analyzeInvocationEdge");
            return newData;
        }

        CtMethod<?> methodTo = (CtMethod<?>) edge.getTo();
        if (methodTo == null) {
            return newData;
        }
        CtInvocation<?> ctInvocation = (CtInvocation<?>) edge.getCallExpression();

        int argInd = 0;
        if (ctInvocation.getArguments().size() != methodTo.getParameters().size()) {
            return newData;
        }
        for (CtExpression<?> arg : ctInvocation.getArguments()) {
            // Если это переменная - получить её имя
            if (arg instanceof CtVariableRead) {
                String argName = ((CtVariableRead<?>) arg).getVariable().getSimpleName();
                if (argName.equals(datumName)) {
                    if (methodTo.getDeclaringType() instanceof CtInterface<?>) {
//                        System.out.println("datum ушла в метод интерфейса " + methodTo.getSimpleName());
//                        System.out.println(structureSpoon.getImplementationMap().get(methodTo));

                        if (structureSpoon.getImplementationMap() != null && structureSpoon.getImplementationMap().containsKey(methodTo)) {
                            for (CtMethod<?> methodImpl : structureSpoon.getImplementationMap().get(methodTo)) {
                                newData.add(new MethodArgument(
                                        methodImpl.getParameters().get(argInd),
                                        datumName,
                                        edge.getFrom()
                                ));
    //                            newData.get(newData.size() - 1).getLocation().print(0);
                            }
                        }
                    } else {
                        newData.add(new MethodArgument(
                                methodTo.getParameters().get(argInd),
                                datumName,
                                edge.getFrom()
                        ));
                    }
                }
            }
            ++argInd;
        }
        return newData;
    }

    private List<FollowedDatum> getNewDatumFromConstructorCall(String datumName, Edge edge) {
        List<FollowedDatum> newData = new ArrayList<>();
        if (!(edge.getCallExpression() instanceof CtConstructorCall<?>)) {
            System.out.println("ошибочный вызов метода analyzeConstructorCallEdge");
            return newData;
        }

        int argInd = 0;
        CtConstructor<?> constructorTo = (CtConstructor<?>) edge.getTo();
        if (constructorTo == null) {
            return newData;
        }
        CtConstructorCall<?> ctConstructorCall = (CtConstructorCall<?>) edge.getCallExpression();
        if (ctConstructorCall.getArguments().size() != constructorTo.getParameters().size()) {
            return newData;
        }
        for (CtExpression<?> arg : ctConstructorCall.getArguments()) {
            // Если это переменная - получить её имя
            if (arg instanceof CtVariableRead) {
                String argName = ((CtVariableRead<?>) arg).getVariable().getSimpleName();
                if (argName.equals(datumName)) {
                    newData.add(new MethodArgument(
                            constructorTo.getParameters().get(argInd),
                            datumName,
                            edge.getFrom()
                    ));
                }
            }
            ++argInd;
        }
        return newData;
    }
}

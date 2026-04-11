package org.example.analizer;

import org.example.analizer.followed_data.*;
import org.example.analizer.operations.Operation;
import org.example.analizer.operations.OperationArgumentOfMethod;
import org.example.analizer.project_structure.ProjectStructureGraph;
import org.example.analizer.project_structure.ProjectStructureNode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassAnaliser {
    private ProjectStructureGraph projectStructureGraph;

    public ClassAnaliser(ProjectStructureGraph projectStructureGraph) {
        this.projectStructureGraph = projectStructureGraph;
//        projectStructureGraph.getStructure().print();
    }

    public Queue<FollowedData> analiseClass(FollowedData data) {
        Queue<FollowedData> queue = new ArrayDeque<>();
        Queue<FollowedData> queueForNextClass = new ArrayDeque<>();
        queue.add(data);

        while (!queue.isEmpty()) {
            // извлекает и удаляет
            FollowedData curData = queue.poll();
            // что делать с подклассами? и надо ли это учитывать??
            // если новые найденные data находятся в другом классе - отложить их и не учитывать
            if (!curData.getJavaClass().equals(data.getJavaClass())) {
                queueForNextClass.add(data);
            } else {
                System.out.println("data ::: " + curData.getName());
                ProjectStructureNode method = projectStructureGraph.getMethod(
                        curData.getJavaPackage(), curData.getJavaClass(), curData.getMethod().getName()
                );
                method.print(0);
                if (method != null) {
                    ArrayList<FollowedData> nextDatasToFollowed = analiseMethod(curData, method);
                    for (FollowedData nextData : nextDatasToFollowed) {
                        // TODO нужно с картой проекта сверяться ещё, чтобы не ловиться на циклах
                        if (!nextData.isAlreadyAnalized()) {
                            queue.add(nextData);
                            nextData.setAlreadyAnalized();
                        }
                    }
                } else {
                    System.out.println("Нет такого метода");
                }
            }
        }
        data.setAlreadyAnalized();
        return queueForNextClass;
    }

    private ArrayList<FollowedData> analiseMethod(FollowedData data, ProjectStructureNode method) {
//        System.out.println("analiseMethod ::: " + data.getMethod().getName() + ", PositionInMethod = " + data.getPositionInMethod());
//        for (int i = 0; i < method.getChildren().size(); ++i) {
//            System.out.println(String.format("argument ::: %s, PositionInMethod = %d", method.getChildren().get(i).getName(), i + 1));
//        }
//        data.setName(method.getChildren().get(data.getPositionInMethod() - 1).getName());
        String[] content = method.getContent();
        System.out.println("in analiseMethod");
        data.print();

        // нашли все операции связанные с искомой data и теперь хотим их распарсить
        ArrayList<Operation> operations = new ArrayList<>();
        for (String operationContent : content) {
            operations.addAll(parseStrToOperations(operationContent, data));
        }
        // надо распарсить все операции в структуру data
        ArrayList<FollowedData> newDatas = new ArrayList<>();
        for (Operation operation : operations) {
            newDatas.add(getNewFollowedDataFromOperation(operation, data, content));
        }

        return newDatas;
    }

    private FollowedData getNewFollowedDataFromOperation(Operation operation, FollowedData data, String[] content) {
        FollowedData newData = new FollowedData();
        if (operation instanceof OperationArgumentOfMethod) {
            System.out.println("instanceof OperationArgumentOfMethod");
//            return null;
            if (Objects.equals(((OperationArgumentOfMethod) operation).getOperationPerformerClass(), data.getJavaClass())) {
                // TODO надо делать в логике класса
                // TODO особенно если пытаться искать имя переменной
//                System.out.println("instanceof OperationArgumentOfMethod");
                newData = new FollowedData(
                        data.getFile(),
                        data.getJavaPackage(),
                        data.getJavaClass(),
                        ((OperationArgumentOfMethod) operation).getMethod());
                newData.setPositionInMethod(((OperationArgumentOfMethod) operation).getPositionNumber());
                newData.print();
//                newDatas.add(newData);
            } else {
                // TODO надо делать в логике метода
                // искать конструктор или аргумент метода, чтобы узнать класс или интерфейс и тд
                // или создание через присваивание

                // проверка, что он является аргументом метода
                // TODO

                // создание через присваивание + конструкторы
                String initRegex = "([a-zA-Z0-9_<>]+)\\s+" +
                        ((OperationArgumentOfMethod) operation).getOperationPerformerName() +
                        "\\s+=\\s+";

                Pattern initPattern = Pattern.compile(initRegex);
                for (String line : content) {
                    Matcher initMatcher = initPattern.matcher(line);
                    if (initMatcher.find()) {
                        newData.print();
//                        System.out.println(initMatcher.group());
                        String constructorRegex = "([a-zA-Z0-9_<>]+)\\s+" +
                                ((OperationArgumentOfMethod) operation).getOperationPerformerName() +
                                "\\s+=\\s+new\\s+(\\w+)";
                        Pattern constructorPattern = Pattern.compile(constructorRegex);
                        Matcher matcher = constructorPattern.matcher(initMatcher.group());
                        if (matcher.matches()) {
                            System.out.println("constructor :::");
                            System.out.println(initMatcher.group());
                            // TODO значения новой data нужно найти в файле
                            // file
                            // package
                            newData = new FollowedData(
                                    data.getFile(),
                                    data.getJavaPackage(),
                                    matcher.group(2),
                                    ((OperationArgumentOfMethod) operation).getMethod());
                            newData.setPositionInMethod(((OperationArgumentOfMethod) operation).getPositionNumber());
//                                newDatas.add(newData);
                            break;
                        }
                        constructorRegex = "(\\b[^)]+)\\s*" +
                                ((OperationArgumentOfMethod) operation).getOperationPerformerName() +
                                "\\s*=\\s*(\\w+)\\s*(";
                        Pattern equatingPattern = Pattern.compile(constructorRegex);
                        matcher = equatingPattern.matcher(initMatcher.group());
                        if (matcher.matches()) {
                            System.out.println("equating :::");
                            System.out.println(initMatcher.group());
                            // найти этот метод и тип его возвращаемого значения
//                                FollowedData newData = new FollowedData(
//                                        data.getFile(),
//                                        data.getJavaPackage(),
//                                        matcher.group(2),
//                                        ((OperationArgumentOfMethod) operation).getMethod(),
//                                        ((OperationArgumentOfMethod) operation).getOperationSubjectName());
                            break;
                        } else {
                            System.out.println("Strange string, cant get newData :::");
                            System.out.println(initMatcher.group());
                        }
                    }

                }
            }
        }
        return newData;
    }

    private ArrayList<Operation> parseStrToOperations(String operation, FollowedData data) {
//        FollowedData newData = new FollowedData();
        ArrayList<Operation> operations = new ArrayList<>();
        // анализ паттернов

        // TODO ситуация method( ... , method2(data) , ... , data , ... , ...)
        // TODO ситуация = new method ( ... )


        OperationArgumentOfMethod operationArgumentOfMethod = new OperationArgumentOfMethod();
//        System.out.println("operationStr ::: " + operation);
        String methodFromAnotherClassRegex = "(\\w+)\\s+\\.\\s*(\\w+)\\s*\\(([^)]*\\b" + data.getName() + "\\b[^)]*)\\)";
//        int maxTotalGroups = 3;

        Pattern patternFromAnotherClass = Pattern.compile(methodFromAnotherClassRegex);
        Matcher matcher = patternFromAnotherClass.matcher(operation);

        // ищем в какой метод ушла дата
        while (matcher.find()) {
            System.out.println("methodFromAnotherClassRegex ::: " + methodFromAnotherClassRegex);
            System.out.println("found ::: " + matcher.group());
            // количество аргументов
            int argumentsCount = matcher.group(3).split(",").length;
            operationArgumentOfMethod.setMethod(new Method(matcher.group(2), argumentsCount));
            int dataArgumentPosition = matcher.group(3).split(data.getName()).length;
            operationArgumentOfMethod.setPositionNumber(dataArgumentPosition);
            operationArgumentOfMethod.setOperationPerformerName(matcher.group(1).split("\\.")[0]);
            operations.add(operationArgumentOfMethod);
        }

        String methodFromSameClassRegex = "new\\s*(\\w+)\\s*\\(([^)]*\\b" + data.getName() + "\\b[^)]*)\\)";
//        int maxTotalGroups = 3;

        Pattern patternFromSameClass = Pattern.compile(methodFromSameClassRegex);
        matcher = patternFromSameClass.matcher(operation);

        // ищем в какой метод ушла дата
        while (matcher.find()) {
            System.out.println("methodFromSameClassRegex ::: " + methodFromSameClassRegex);
            System.out.println("found ::: " + matcher.group());
            int argumentsCount = matcher.group(2).split(",").length;
            operationArgumentOfMethod.setMethod(new Method(matcher.group(1), argumentsCount));
            int dataArgumentPosition = matcher.group(2).split(data.getName()).length;
            operationArgumentOfMethod.setPositionNumber(dataArgumentPosition);
            operationArgumentOfMethod.setOperationPerformerClass(data.getJavaClass());
            operations.add(operationArgumentOfMethod);
        }

//        String equatingRegex = "([a-z]+[a-zA-Z0-9_])\\s*=\\s*" + data.getData() + "\\s*;";
//        maxTotalGroups = 6;
//
//        pattern = Pattern.compile(equatingRegex);
//        matcher = pattern.matcher(operation);
//
//        // TODO анализ тернарного и других операций ???
//        // ищем в какой метод ушла дата
//        while (matcher.find()) {
//            //
//        }
        // TODO анализ тернарного и других операций ???
        // далее надо найти где этот метод живёт , в нём переменная по-другому называется)
        return operations;
    }

//    public void print(FollowedData data) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"))) {
//            writer.write(data.getJavaClass() + "::");
//            writer.write(data.getMethod() + "::");
//            writer.write(data.getData());
//            writer.newLine();
//            for (int i = 0; i < data.getOperationsCount(); ++i) {
//                writer.write(data.getOperations().get(i));
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

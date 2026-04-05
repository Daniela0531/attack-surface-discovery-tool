package org.example.analizer;

import org.example.analizer.structures.FollowedData;
import org.example.analizer.structures.Method;
import org.example.analizer.structures.ProjectStructureGraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassAnaliser {
//    private Map<String , ArrayList<String>> edjs = new HashMap<>();

//    public ClassAnaliser(int size) {
//        System.out.println(size);
//        edjs = new HashMap<>();
//    }
    private ProjectStructureGraph projectStructureGraph;
    public ClassAnaliser(ProjectStructureGraph projectStructureGraph) {
        this.projectStructureGraph = projectStructureGraph;
    }

    public Queue<FollowedData> analiseClass(List<String> content , FollowedData data) {
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
                ArrayList<FollowedData> nextDatasToFollowed = analiseOneMethod(content , curData);
                for (FollowedData nextData : nextDatasToFollowed) {
                    if (!nextData.isAlreadyAnalized()) {
                        queue.add(nextData);
                        nextData.setAlreadyAnalized();
                    }
                }
            }
        }
        data.setAlreadyAnalized();
        return queueForNextClass;
    }

    private ArrayList<FollowedData> analiseOneMethod(List<String> content, FollowedData data) {
        boolean nextIsClassName = false;
        boolean isRightClass = false;
        boolean isRightMethod = false;
        for (String line : content) {
            for (String token : line.split("[ , , ()?:]")) {
                if (token.equals("class")) {
                    nextIsClassName = true;
                }
                if (nextIsClassName && token.equals(data.getJavaClass())) {
                    System.out.println("I am in class: " + token + "\n");
                    isRightClass = true;
                }
                if (!token.equals("class")) {
                    nextIsClassName = false;
                }
                if (isRightClass && token.equals(data.getMethod().getName())) {
                    // необходимо проверить, что в методе нужное количество аргументов
                    System.out.println("I am in method: " + token + "\n");
                    isRightMethod = true;
                }
                if (isRightClass && isRightMethod && token.equals(data.getData())) {
                    System.out.println("I am found data: " + token + "\n");
                    data.pushOperation(line);
                }
            }
        }
        // нашли все операции связанные с искомой data и теперь хотим их распарсить
        ArrayList<FollowedData> newDatas = new ArrayList<>();
        for (String operation : data.getOperations()) {
            newDatas.add(parseOperationToData(operation , data));
        }
        // надо распарсить все операции в структуру data
        return newDatas;
    }

    private FollowedData parseOperationToData(String operation, FollowedData data) {
        FollowedData newData = new FollowedData();
        // анализ паттернов

        // TODO ситуация method( ... , method2(data) , ... , data , ... , ...)
        String methodRegex = "(\\w+)?\\s*(\\.)?\\s*(\\w+)\\s*\\(([,\\w+^)]*)(" + data.getData() + ")([,\\w+]*)\\)";
        int maxTotalGroups = 6;

        Pattern pattern = Pattern.compile(methodRegex);
        Matcher matcher = pattern.matcher(operation);

        // TODO анализ тернарного и других операций ???
        // ищем в какой метод ушла дата
        while (matcher.find()) {
            if (matcher.groupCount() == maxTotalGroups) {
                // найти какого класса эта переменная
            } else if (matcher.groupCount() == maxTotalGroups - 2) {
                newData.setJavaPackage(data.getJavaPackage());
                newData.setJavaClass(data.getJavaClass());
                newData.setFile(data.getFile());
                Method method = new Method(matcher.group(1), 0);
                newData.setMehod(method);
                // найти новое значение data
            }
        }

        String equatingRegex = "(\\w+)\\s*=)";
        maxTotalGroups = 6;

        pattern = Pattern.compile(equatingRegex);
        matcher = pattern.matcher(operation);

        // TODO анализ тернарного и других операций ???
        // ищем в какой метод ушла дата
        while (matcher.find()) {
            //
        }
        // далее надо найти где этот метод живёт , в нём переменная по-другому называется)
        return newData;
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

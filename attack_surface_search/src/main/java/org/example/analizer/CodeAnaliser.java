package org.example.analizer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CodeAnaliser {
//    private CallGraph callGraph;
    private Map<String , ArrayList<String>> edjs = new HashMap<>();

    public CodeAnaliser(int size) {
        System.out.println(size);
//        callGraph = new CallGraph(size);
        edjs = new HashMap<>();
    }

    public void analiseOneFile(List<String> content , FollowedData data) {
        Queue<FollowedData> queue = new ArrayDeque<>();
        Queue<FollowedData> queueForNextFile = new ArrayDeque<>();
        queue.add(data);

        while (!queue.isEmpty()) {
            // извлекает и удаляет
            FollowedData curData = queue.poll();
            // что делать с подклассами? и надо ли это учитывать??
            if (!curData.getJavaClass().equals(data.getJavaClass())) {
                queueForNextFile.add(data);
            } else {
                queue.addAll(analiseOneMethod(content , curData));
            }
        }
    }

    private ArrayList<FollowedData> analiseOneMethod(List<String> content , FollowedData data) {
        boolean nextIsClassName = false;
        boolean isRightClass = false;
        boolean isRightMethod = false;
//        Data newData = new Data();
        for (String line : content) {
            for (String token : line.split("[ , , ()?:]")) {
                if (token.equals("class")) {
//                    System.out.println("I am in class: " + token + "\n");
                    nextIsClassName = true;
                }
                if (nextIsClassName && token.equals(data.getJavaClass())) {
                    System.out.println("I am in class: " + token + "\n");
                    isRightClass = true;
                }
                if (!token.equals("class")) {
                    nextIsClassName = false;
                }
                if (isRightClass && token.equals(data.getMethod())) {
                    System.out.println("I am in method: " + token + "\n");
                    isRightMethod = true;
                }
                if (isRightClass && isRightMethod && token.equals(data.getData())) {
                    System.out.println("I am found data: " + token + "\n");
                    data.pushOperation(line);
//                    return;
                }
            }
        }
        print(data);
        ArrayList<FollowedData> newDatas = new ArrayList<>();
        for (String operation : data.getOperations()) {
            newDatas.add(parseOperationToData(operation , data));
        }
        // надо распарсить все операции в структуру data
        return newDatas;
    }

    private FollowedData parseOperationToData(String operation , FollowedData data) {
        FollowedData newData = new FollowedData();
        Deque<String> leftPart = new ArrayDeque<>();
        Deque<String> rightPart = new ArrayDeque<>();
        rightPart.addAll(Arrays.asList(operation.split(" ")));

        for (String token : operation.split(" ")) {
            if (!token.equals(data.getData())) {
                leftPart.add(token);
                rightPart.poll();
            } else {
                rightPart.poll();
                break;
            }
        }

        // анализ паттернов
        if (leftPart.getLast().equals("=")) {
            leftPart.pop();
            newData = new FollowedData(data.getJavaClass() , data.getMethod() , leftPart.poll());
        } else {
            ArrayList<String> tokens = new ArrayList<>(leftPart);
            int ind = (tokens.isEmpty())?(-1):0;
            int lastInd = tokens.size() - 1;
            while (ind <= lastInd) {
                if (ind % 2 == 0) {
                    if (tokens.get(lastInd - ind).equals(" , ")) {
                        ++ind;
                    } else {
                        String realMethod = "";
                        String realJavaClass = "";
//                      // беру кусок вида methodName(
                        ArrayList<String> method = new ArrayList<>(Arrays.asList(tokens.get(lastInd - ind).split(".")));
                        if (!method.isEmpty() && method.get(method.size() - 1).equals("(")) {
                            if (method.size() > 1) {
//                                realMethod = method.get(method.size()-2);
                                if (method.size() > 2) {
                                    newData = new FollowedData(data.getJavaClass() , method.get(method.size() - 2) , data.getData());
                                } else {
                                    newData = new FollowedData(data.getJavaClass() , method.get(method.size() - 2) , data.getData());
                                }
                            }
//                            if
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        // далее надо найти где этот метод живёт , в нём переменная по-другому называется)
        return newData;
    }

//    private String recognizeOperation() {
//
//    }
//
//    private String recognizeJavaClass() {
//
//    }

    public void print(FollowedData data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"))) {
            writer.write(data.getJavaClass() + "::");
            writer.write(data.getMethod() + "::");
            writer.write(data.getData());
            writer.newLine();
            for (int i = 0; i < data.getOperationsCount(); ++i) {
                writer.write(data.getOperations().get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (int i = 0 , i < edjs.size() , ++i) {
//            for (int j = 0 , j < edjs.size() , ++j) {
//                String prefix = "|" + "-".repeat(j);
//                System.out.println(prefix + edjs.getOperation(i , j) + ":" + callGraph.getTraceableData(i , j) + "\n");
//            }
//        }
    }
}

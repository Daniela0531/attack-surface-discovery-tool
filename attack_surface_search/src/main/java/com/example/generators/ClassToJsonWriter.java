package com.example.generators;

import com.example.analizer.followed_data.ClassField;
import com.example.analizer.followed_data.FollowedDatum;
import com.example.analizer.followed_data.LocalVariableInMethod;
import com.example.analizer.followed_data.MethodArgument;
import com.example.analizer.followed_data.operation.AssignmentInMethod;
import com.example.result_structure.ResultEdge;
import com.example.result_structure.ResultGraph;
import com.fasterxml.jackson.databind.ObjectMapper;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
//import org.example.structure.StructureEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassToJsonWriter {
    private Path target;
    private ObjectMapper mapper = new ObjectMapper();
    public ClassToJsonWriter(Path target) {
        this.target = target;
        this.mapper = new ObjectMapper();
    }
//
    public void writeToJson(List<ResultGraph> results) throws Exception {
        Files.write(
                target,
                "{\n  \"name\": \"root\",\n  \"children\": [".getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        Set<FollowedDatum> visited = new HashSet<>();
//        System.out.print("DFS (рекурсивный): ");
        int i = 2;
        for (int j = 0; j < results.size(); ++j) {
            dfsRecursivePrint(results.get(j), results.get(j).getStart(), visited, 1, i);
            if (j < results.size() - 1) {
                String jsonString = ",";
                Files.write(
                        target,
                        jsonString.getBytes(),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
            }
        }
        Files.write(
                target,
                "\n  ]\n}".getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }
//
//    private void writeBodyToJson(List<ResultGraph> results, int extraTabs, Boolean isLast) throws Exception {
//        String startTabs = "  ".repeat(curEntity.getLevel() + extraTabs);
//        String bodyTabs = "  ".repeat(curEntity.getLevel() + 1 + extraTabs);
//        String contentTabs = "  ".repeat(curEntity.getLevel() + 2 + extraTabs);
//        String type = curEntity.getType();
//        String name = curEntity.getName();
//        ArrayList<String> content = curEntity.getContent();
//
//        String jsonString = startTabs + "{\n" +
//                bodyTabs + "\"type\": \"" + type + "\",\n" +
//                bodyTabs + "\"name\": \"" + name + "\",\n";
//        jsonString += bodyTabs + "\"children\": [";
//        if (curEntity.getChildren().isEmpty()) {
//            jsonString += "]\n" + startTabs + "}";
//            if (!isLast) {
//                jsonString += ",\n";
//            } else {
//                jsonString += "\n";
//            }
//            Files.write(
//                    target,
//                    jsonString.getBytes(),
//                    StandardOpenOption.CREATE,
//                    StandardOpenOption.APPEND);
//            return;
//        }
//        extraTabs += 1;
//        jsonString += "\n";
//        Files.write(
//                target,
//                jsonString.getBytes(),
//                StandardOpenOption.CREATE,
//                StandardOpenOption.APPEND);
//        ArrayList<StructureEntity> children = curEntity.getChildren();
//        boolean childIsLast = false;
//        for (int i = 0; i < children.size(); ++i) {
//            if (i == children.size() - 1) {
//                childIsLast = true;
//            }
//            writeBodyToJson(children.get(i), extraTabs, childIsLast);
//        }
//        String finishString = bodyTabs + "]\n" + startTabs + "}";
//        if (!isLast) {
//            finishString += ",\n";
//        } else {
//            finishString += "\n";
//        }
//        Files.write(
//                target,
//                finishString.getBytes(),
//                StandardOpenOption.CREATE,
//                StandardOpenOption.APPEND);
//
//    }

//    public void print(List<ResultGraph> resultGraphs) {
//        Set<FollowedDatum> visited = new HashSet<>();
////        System.out.print("DFS (рекурсивный): ");
//        int i = 0;
//        for (ResultGraph resultGraph : resultGraphs) {
//            dfsRecursivePrint(resultGraph.getStart(), visited, i);
//        }
////        System.out.println();
//    }

    private void printJson(String str, int tabsInd) throws IOException {
        String tabs = "  ".repeat(tabsInd);
        String childTabs = "  ".repeat(tabsInd + 1);
        String jsonString = "\n" +
                tabs + "{\n" +
                childTabs + "\"name\": " + "\"" + str + "\",\n" +
                childTabs + "\"children\": [";
        Files.write(
                target,
                jsonString.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private void dfsRecursivePrint(ResultGraph resultGraph, FollowedDatum vertex, Set<FollowedDatum> visited, int extraTabsIn, int i) throws IOException {
        visited.add(vertex);
        String tabs = "  ".repeat(i);
        String childTabs = "  ".repeat(i + 1);
        String parentTabs = "  ".repeat((i - 1) < 0 ? 0 : i - 1);
//        System.out.println(tabs + i + " ::");
        if (vertex instanceof MethodArgument) {
            CtExecutable<?> executable = ((MethodArgument)vertex).getLocation();
            if (executable != null) {
                if (executable instanceof CtMethod<?>) {
//                    printJson(((CtMethod<?>) executable).getDeclaringType().getQualifiedName(), i);
                } else if (executable instanceof CtConstructor<?>) {
//                    printJson(((CtConstructor<?>) executable).getDeclaringType().getQualifiedName(), i);
                } else {
                    printJson("Странный родитель метода/конструктора", i);
                }
//                System.out.println(tabs + executable.getSignature());
//                System.out.println(tabs + "from :: " + ((MethodArgument) vertex).getParameterImplName());
//                System.out.println(tabs + "to parameter :: " + vertex.getName());
//                String jsonString = "\n" + tabs + executable.getSignature() + "\n" +
//                        tabs + ((MethodArgument) vertex).getParameterImplName() + "\n" +
//                        tabs + vertex.getName();
                printJson(executable.getSignature(), i);
            }
        } else if (vertex instanceof LocalVariableInMethod) {
            CtExecutable<?> executable = ((LocalVariableInMethod) vertex).getLocation();
            if (executable != null) {
                if (executable instanceof CtMethod<?>) {
//                    printJson(((CtMethod<?>) executable).getDeclaringType().getQualifiedName(), i);
                } else if (executable instanceof CtConstructor<?>) {
//                    printJson(((CtConstructor<?>) executable).getDeclaringType().getQualifiedName(), i);
                } else {
                    printJson("Странный родитель метода/конструктора", i);
                }
//                System.out.println(tabs + executable.getSignature());
//                System.out.println(tabs + "local_variable :: " + vertex.getName());
//                String jsonString = "\n" + tabs + executable.getSignature() + "\n" +
//                        tabs + "local_variable :: " + vertex.getName();
                printJson(executable.getSignature(), i);
            }
        } else if (vertex instanceof AssignmentInMethod) {
            CtExecutable<?> executable = ((AssignmentInMethod) vertex).getLocation();
            if (executable != null) {
                if (executable instanceof CtMethod<?>) {
//                    printJson(((CtMethod<?>) executable).getDeclaringType().getQualifiedName(), i);
                } else if (executable instanceof CtConstructor<?>) {
//                    printJson(((CtConstructor<?>) executable).getDeclaringType().getQualifiedName(), i);
                } else {
                    printJson("Странный родитель метода/конструктора", i);
                }
//                System.out.println(tabs + executable.getSignature());
//                System.out.println(tabs + "assignment :: " + vertex.getName());
//                String jsonString = "\n" + tabs + executable.getSignature() +
//                        tabs + "assignment :: " + vertex.getName();
                printJson(executable.getSignature(), i);
            }
        } else if (vertex instanceof ClassField) {
            CtClass<?> ctClass = ((ClassField) vertex).getLocation();
            if (ctClass == null) {
//                System.out.println(tabs + "class :: " + ctClass.getSimpleName());
//                System.out.println(tabs + "field :: " + vertex.getName());
//                String jsonString = "\n" + tabs + "class :: " + ctClass.getSimpleName() +
//                        tabs + "field :: " + vertex.getName();
                printJson(vertex.getName(), i);
            }
        } else {
//            System.out.println(tabs + "Новый вид Datum!!!!!!!");
            printJson("Новый вид Datum!!!!!!!", i);
        }

        List<FollowedDatum> neighbors = getNeighbors(vertex, resultGraph);
        if (neighbors.isEmpty()) {
            String jsonString = "]\n" +
                    tabs + "}";
            Files.write(
                    target,
                    jsonString.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            return;
        }
        for (int j = 0; j < neighbors.size(); ++j) {
            if (!visited.contains(neighbors.get(j))) {
                dfsRecursivePrint(resultGraph, neighbors.get(j), visited, extraTabsIn, i + 1);
                if (j < neighbors.size() - 1) {
                    String jsonString = ",";
                    Files.write(
                            target,
                            jsonString.getBytes(),
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND);
                } else {
                    String jsonString = "\n" + tabs +"]\n" +
                            parentTabs + "}";
                    Files.write(
                            target,
                            jsonString.getBytes(),
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND);
                }
            }
        }
    }

    public ArrayList<FollowedDatum> getNeighbors(FollowedDatum vertex, ResultGraph resultGraph) {
        ArrayList<FollowedDatum> neighbors = new ArrayList<>();
        for (ResultEdge edge : resultGraph.getEdges()) {
            if (edge.getFrom() == vertex)
                neighbors.add(edge.getTo());
        }
        return neighbors;
    }
}

package com.example.generators;

import com.example.analizer_trace.followed_data.ClassField;
import com.example.analizer_trace.followed_data.FollowedDatum;
import com.example.analizer_trace.followed_data.LocalVariableInMethod;
import com.example.analizer_trace.followed_data.MethodArgument;
import com.example.analizer_trace.followed_data.AssignmentInMethod;
import com.example.result_structure.ResultEdge;
import com.example.result_structure.ResultTrace;
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
    public void writeToJson(List<ResultTrace> results) throws Exception {
        Files.write(
                target,
                "".getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        Set<FollowedDatum> visited = new HashSet<>();
//        System.out.print("DFS (рекурсивный): ");
        int i = 1;
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

    public void writeToJson(ResultTrace result) throws Exception {
//        Files.write(
//                target,
//                ,
//                StandardOpenOption.CREATE,
//                StandardOpenOption.APPEND);
        Set<FollowedDatum> visited = new HashSet<>();
//        System.out.print("DFS (рекурсивный): ");
        int i = 0;
//        for (int j = 0; j < result.size(); ++j) {
            dfsRecursivePrint(result, result.getStart(), visited, 1, i);
//            if (j < result.size() - 1) {
//                String jsonString = ",";
//                Files.write(
//                        target,
//                        jsonString.getBytes(),
//                        StandardOpenOption.CREATE,
//                        StandardOpenOption.APPEND);
//            }
//        }
//        Files.write(
//                target,
//                "\n  ]\n}".getBytes(),
//                StandardOpenOption.CREATE,
//                StandardOpenOption.APPEND);
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

    private void printJson(OperytionType type, String methodSignature, int tabsInd) throws IOException {
        String tabs = "  ".repeat(tabsInd);
        String childTabs = "  ".repeat(tabsInd + 1);
        String jsonString = "\n" +
                tabs + "{\n" +
                childTabs + "\"type\": " + "\"" + type.toString() + "\",\n";
        switch (type) {
            case ASSIGNMENT -> jsonString +=
                    childTabs + "\"from_variable\": " + "\"" + methodSignature + "\",\n" +
                    childTabs + "\"to_param\": " + "\"" + methodSignature + "\",\n";
            case UNKNOWN -> jsonString +=
                    childTabs + "\"operation\": " + "\"" + methodSignature + "\",\n";
        }
        jsonString += childTabs + "\"children\": [";
        Files.write(
                target,
                jsonString.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private void printAssignmentToJson(AssignmentInMethod vertex, int tabsInd) throws IOException {
        String tabs = "  ".repeat(tabsInd);
        String childTabs = "  ".repeat(tabsInd + 1);
        String jsonString = "\n" +
                tabs + "{\n" +
                childTabs + "\"type\": " + "\"" + "ASSIGNMENT" + "\",\n" +
                childTabs + "\"from_variable\": " + "\"" + vertex.getAssignmentParam() + "\",\n" +
                childTabs + "\"to_param\": " + "\"" + vertex.getName() + "\",\n";
        jsonString += childTabs + "\"children\": [";
        Files.write(
                target,
                jsonString.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private void printLocalVariableToJson(LocalVariableInMethod vertex, int tabsInd) throws IOException {
        String tabs = "  ".repeat(tabsInd);
        String childTabs = "  ".repeat(tabsInd + 1);
        String jsonString = "\n" +
                tabs + "{\n" +
                childTabs + "\"type\": " + "\"" + "ASSIGNMENT" + "\",\n" +
//                childTabs + "\"from_variable\": " + "\"" + vertex.getAssignmentParam() + "\",\n" +
                childTabs + "\"to_param\": " + "\"" + vertex.getName() + "\",\n";
        jsonString += childTabs + "\"children\": [";
        Files.write(
                target,
                jsonString.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private void printFieldToJson(ClassField vertex, int tabsInd) throws IOException {
        String tabs = "  ".repeat(tabsInd);
        String childTabs = "  ".repeat(tabsInd + 1);
        String jsonString = "\n" +
                tabs + "{\n" +
                childTabs + "\"type\": " + "\"" + "ASSIGNMENT" + "\",\n" +
//                childTabs + "\"from_variable\": " + "\"" + vertex.getAssignmentParam() + "\",\n" +
                childTabs + "\"to_param\": " + "\"" + vertex.getName() + "\",\n";
        jsonString += childTabs + "\"children\": [";
        Files.write(
                target,
                jsonString.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private void printMethodJson(MethodArgument vertex, int tabsInd) throws IOException {
        String tabs = "  ".repeat(tabsInd);
        String childTabs = "  ".repeat(tabsInd + 1);
        String jsonString = "\n" +
                tabs + "{\n" +
                childTabs + "\"type\": " + "\"" + "METHOD_PARAM" + "\",\n";
        if (vertex.getLocation() instanceof CtMethod<?>)
            jsonString += childTabs + "\"class\": " + "\"" + ((CtMethod<?>)vertex.getLocation()).getDeclaringType().getSimpleName() + "\",\n";
        else
            jsonString += childTabs + "\"class\": " + "\"" + ((CtConstructor<?>)vertex.getLocation()).getDeclaringType().getSimpleName() + "\",\n";

        jsonString += childTabs + "\"methodSignature\": " + "\"" + vertex.getLocation().getSignature() + "\",\n";

        if (vertex.getParameterImplName() != null)
            jsonString += childTabs + "\"from_variable\": " + "\"" + vertex.getParameterImplName() + "\",\n";
        jsonString += childTabs + "\"to_param\": " + "\"" + vertex.getName() + "\",\n";
        jsonString += childTabs + "\"is_external\": " + vertex.isOutsideLib() + ",\n";
        jsonString += childTabs + "\"children\": [";
        Files.write(
                target,
                jsonString.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private void dfsRecursivePrint(ResultTrace resultTrace, FollowedDatum vertex, Set<FollowedDatum> visited, int extraTabsIn, int i) throws IOException {
        visited.add(vertex);
        String tabs = "  ".repeat(i);
        String childTabs = "  ".repeat(i + 1);
        String parentTabs = "  ".repeat((i - 1) < 0 ? 0 : i - 1);
//        System.out.println(tabs + i + " ::");
        if (vertex instanceof MethodArgument) {
            CtExecutable<?> executable = ((MethodArgument)vertex).getLocation();
            if (executable != null) {
                if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
                    printMethodJson((MethodArgument) vertex, i);
                }
//                else {
//                    printJson(METHOD_PARAM, vertex,"Странный родитель метода/конструктора", i);
//                }
//                System.out.println(tabs + executable.getSignature());
//                System.out.println(tabs + "from :: " + ((MethodArgument) vertex).getParameterImplName());
//                System.out.println(tabs + "to parameter :: " + vertex.getName());
//                String jsonString = "\n" + tabs + executable.getSignature() + "\n" +
//                        tabs + ((MethodArgument) vertex).getParameterImplName() + "\n" +
//                        tabs + vertex.getName();

            }
        } else if (vertex instanceof LocalVariableInMethod) {
            CtExecutable<?> executable = ((LocalVariableInMethod) vertex).getLocation();
            if (executable != null) {
                if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
                    printLocalVariableToJson((LocalVariableInMethod) vertex, i);
                }
//                else {
//                    printLocalVariableToJson(ASSIGNMENT, "Странный родитель метода/конструктора", i);
//                }
//                System.out.println(tabs + executable.getSignature());
//                System.out.println(tabs + "local_variable :: " + vertex.getName());
//                String jsonString = "\n" + tabs + executable.getSignature() + "\n" +
//                        tabs + "local_variable :: " + vertex.getName();

            }
        } else if (vertex instanceof AssignmentInMethod) {
            CtExecutable<?> executable = ((AssignmentInMethod) vertex).getLocation();
            if (executable != null) {
                if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
                    printAssignmentToJson((AssignmentInMethod) vertex, i);
                }
//                else {
//                    printJson(ASSIGNMENT, "Странный родитель метода/конструктора", i);
//                }
//                System.out.println(tabs + executable.getSignature());
//                System.out.println(tabs + "assignment :: " + vertex.getName());
//                String jsonString = "\n" + tabs + executable.getSignature() +
//                        tabs + "assignment :: " + vertex.getName();

            }
        } else if (vertex instanceof ClassField) {
            CtClass<?> ctClass = ((ClassField) vertex).getLocation();
            if (ctClass == null) {
//                System.out.println(tabs + "class :: " + ctClass.getSimpleName());
//                System.out.println(tabs + "field :: " + vertex.getName());
//                String jsonString = "\n" + tabs + "class :: " + ctClass.getSimpleName() +
//                        tabs + "field :: " + vertex.getName();
                printFieldToJson((ClassField) vertex, i);
            }
        }
//        else {
////            System.out.println(tabs + "Новый вид Datum!!!!!!!");
//            printJson(UNKNOWN, "Новый вид Datum!!!!!!!", i);
//        }

        List<FollowedDatum> neighbors = getNeighbors(vertex, resultTrace);
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
                dfsRecursivePrint(resultTrace, neighbors.get(j), visited, extraTabsIn, i + 2);
                if (j < neighbors.size() - 1) {
                    String jsonString = ",";
                    Files.write(
                            target,
                            jsonString.getBytes(),
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND);
                } else {
                    String jsonString = "\n" + childTabs +"]\n" +
                            tabs + "}";
                    Files.write(
                            target,
                            jsonString.getBytes(),
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND);
                }
            }
        }
    }

    public ArrayList<FollowedDatum> getNeighbors(FollowedDatum vertex, ResultTrace resultTrace) {
        ArrayList<FollowedDatum> neighbors = new ArrayList<>();
        for (ResultEdge edge : resultTrace.getEdges()) {
            if (edge.getFrom() == vertex)
                neighbors.add(edge.getTo());
        }
        return neighbors;
    }
}

enum OperytionType {
    ASSIGNMENT, METHOD_PARAM, UNKNOWN
}

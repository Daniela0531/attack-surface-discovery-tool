package com.example.result_structure;

import com.example.analizer_trace.followed_data.*;
import com.example.analizer_trace.followed_data.AssignmentInMethod;
import spoon.reflect.declaration.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResultTrace {
    FollowedDatum start = null;
    List<FollowedDatum> nodes = new ArrayList<>();
    List<ResultEdge> edges = new ArrayList<>();

    public ResultTrace(FollowedDatum start) {
        this.start = start;
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public void print() {
        Set<FollowedDatum> visited = new HashSet<>();
//        System.out.print("DFS (рекурсивный): ");
        int i = 0;
        dfsRecursivePrint(start, visited, i);
//        System.out.println();
    }

    private void dfsRecursivePrint(FollowedDatum vertex, Set<FollowedDatum> visited, int i) {
        visited.add(vertex);
        String tabs = "   ".repeat(i);
        System.out.println(tabs + i + " ::");
        if (vertex instanceof MethodArgument) {
            CtExecutable<?> executable = ((MethodArgument)vertex).getLocation();
            if (executable != null) {
                if (executable instanceof CtMethod<?>) {
                    System.out.println(tabs + "class :: " + ((CtMethod<?>) executable).getDeclaringType().getQualifiedName());
                } else if (executable instanceof CtConstructor<?>) {
                    System.out.println(tabs + "class :: " + ((CtConstructor<?>) executable).getDeclaringType().getQualifiedName());
                } else {
                    System.out.println(tabs + "Странный родитель метода/конструктора");
                }
                System.out.println(tabs + executable.getSignature());
                System.out.println(tabs + "from :: " + ((MethodArgument) vertex).getParameterImplName());
                System.out.println(tabs + "to parameter :: " + vertex.getName());
            }
        } else if (vertex instanceof LocalVariableInMethod) {
            CtExecutable<?> executable = ((LocalVariableInMethod) vertex).getLocation();
            if (executable != null) {
                if (executable instanceof CtMethod<?>) {
                    System.out.println(tabs + "class :: " + ((CtMethod<?>) executable).getDeclaringType().getQualifiedName());
                } else if (executable instanceof CtConstructor<?>) {
                    System.out.println(tabs + "class :: " + ((CtConstructor<?>) executable).getDeclaringType().getQualifiedName());
                } else {
                    System.out.println(tabs + "Странный родитель метода/конструктора");
                }
                System.out.println(tabs + executable.getSignature());
                System.out.println(tabs + "local_variable :: " + vertex.getName());
            }
        } else if (vertex instanceof AssignmentInMethod) {
            CtExecutable<?> executable = ((AssignmentInMethod) vertex).getLocation();
            if (executable != null) {
                if (executable instanceof CtMethod<?>) {
                    System.out.println(tabs + "class :: " + ((CtMethod<?>) executable).getDeclaringType().getQualifiedName());
                } else if (executable instanceof CtConstructor<?>) {
                    System.out.println(tabs + "class :: " + ((CtConstructor<?>) executable).getDeclaringType().getQualifiedName());
                } else {
                    System.out.println(tabs + "Странный родитель метода/конструктора");
                }
                System.out.println(tabs + executable.getSignature());
                System.out.println(tabs + "assignment :: " + vertex.getName());
            }
        } else if (vertex instanceof ClassField) {
            CtClass<?> ctClass = ((ClassField) vertex).getLocation();
            if (ctClass == null) {
                System.out.println(tabs + "class :: " + ctClass.getSimpleName());
                System.out.println(tabs + "field :: " + vertex.getName());
            }
        } else {
            System.out.println(tabs + "Новый вид Datum!!!!!!!");
        }

        for (FollowedDatum neighbor : getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsRecursivePrint(neighbor, visited, i + 1);
            }
        }
    }

    public ArrayList<FollowedDatum> getNeighbors(FollowedDatum vertex) {
        ArrayList<FollowedDatum> neighbors = new ArrayList<>();
        for (ResultEdge edge : edges) {
            if (edge.getFrom() == vertex)
                neighbors.add(edge.getTo());
        }
        return neighbors;
    }

    public void addNode(FollowedDatum node) {
        nodes.add(node);
    }
    public boolean isContainsNode(FollowedDatum node) {
        return nodes.contains(node);
    }

    public void addEdge(ResultEdge resultEdge) {
        edges.add(resultEdge);
    }

    public boolean isContainsEdge(ResultEdge edge) {
        return edges.contains(edge);
    }

    public List<ResultEdge> getEdges() {
        return edges;
    }

    public List<FollowedDatum> getNodes() {
        return nodes;
    }
    public FollowedDatum getStart() {
        return start;
    }
}
//public class ResultGraph {
//    ResultNode start = null;
//    List<ResultNode> nodes = new ArrayList<>();
//    List<ResultEdge> edges = new ArrayList<>();
//
//    public ResultGraph(ResultNode start) {
//        this.start = start;
//        this.nodes = new ArrayList<>();
//        this.edges = new ArrayList<>();
//    }
//
//    public ArrayList<ResultNode> getNeighbors(ResultNode vertex) {
//        ArrayList<ResultNode> neighbors = new ArrayList<>();
//        for (ResultEdge edge : edges) {
//            if (edge.getFrom() == vertex)
//                neighbors.add(edge.getTo());
//        }
//        return neighbors;
//    }
//
//    public void print() {
//        Set<ResultNode> visited = new HashSet<>();
////        System.out.print("DFS (рекурсивный): ");
//        int i = 0;
//        dfsRecursiveUtil(start, visited, i);
////        System.out.println();
//    }
//
//    private void dfsRecursiveUtil(ResultNode vertex, Set<ResultNode> visited, int i) {
//        visited.add(vertex);
//        vertex.print(i);
//
//        for (ResultNode neighbor : getNeighbors(vertex)) {
//            if (!visited.contains(neighbor)) {
//                dfsRecursiveUtil(neighbor, visited, i + 1);
//            }
//        }
//    }
//
//    public void addNode(ResultNode node) {
//        nodes.add(node);
//    }
//    public boolean isContainsNode(ResultNode node) {
//        return nodes.contains(node);
//    }
//
//    public void addEdge(ResultEdge resultEdge) {
//        edges.add(resultEdge);
//    }
//
//    public boolean isContainsEdge(ResultEdge edge) {
//        return edges.contains(edge);
//    }
//}

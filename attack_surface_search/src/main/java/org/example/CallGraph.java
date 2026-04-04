package org.example;

import java.util.ArrayList;
import java.util.UUID;

public class CallGraph {
    private class Node {
        private long id = 0;
        private String operation = "";
        private String traceableData = "";
        private ArrayList<String> children = new ArrayList<>();

        public Node() {
            this.children = new ArrayList<>();
            this.id = Math.abs(UUID.randomUUID().getMostSignificantBits());
        }

        public Node(String operation , String traceableData) {
            this.operation = operation;
            this.traceableData = traceableData;
            this.children = new ArrayList<>();
            this.id = Math.abs(UUID.randomUUID().getMostSignificantBits());
        }

        public Node(String operation , String traceableData , ArrayList<String> children) {
            this.operation = operation;
            this.traceableData = traceableData;
            this.children = children;
            this.id = Math.abs(UUID.randomUUID().getMostSignificantBits());
        }
    }

    private ArrayList<ArrayList<Node>> graph = new ArrayList<>();

    public CallGraph() {
        graph = new ArrayList<>();
    }
    public CallGraph(int size) {
        graph = new ArrayList<>(size);
        for (int i = 0; i < size ; ++i) {
            graph.set(i , new ArrayList<>(size));
        }
    }

    public int size() {
        return graph.size();
    }

    public String getOperation(int i , int j) {
        return graph.get(i).get(j).operation;
    }

    public String getTraceableData(int i , int j) {
        return graph.get(i).get(j).traceableData;
    }
}

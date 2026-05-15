package com.example.result_structure;

import com.example.analizer.followed_data.FollowedDatum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResultGraph {
    FollowedDatum start = null;
    List<FollowedDatum> nodes = new ArrayList<>();
    List<ResultEdge> edges = new ArrayList<>();

    public ResultGraph(FollowedDatum start) {
        this.start = start;
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public void print() {
        Set<FollowedDatum> visited = new HashSet<>();
//        System.out.print("DFS (рекурсивный): ");
        int i = 0;
        dfsRecursiveUtil(start, visited, i);
//        System.out.println();
    }

    private void dfsRecursiveUtil(FollowedDatum vertex, Set<FollowedDatum> visited, int i) {
        visited.add(vertex);
        vertex.getLocation().print(i);

        for (FollowedDatum neighbor : getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsRecursiveUtil(neighbor, visited, i + 1);
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

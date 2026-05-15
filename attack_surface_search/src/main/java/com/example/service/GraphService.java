package com.example.service;

import com.example.analizer.AnalyzerAfterSpoonForDatum;
import com.example.result_structure.ResultGraph;
import com.example.structure.StructureSpoon;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GraphService {

    private ResultGraph graph;

    public void analyzeProject() throws Exception {
        // Здесь вызываете ваш анализатор
        // AnalyzerAfterSpoonForDatum analyzer = new AnalyzerAfterSpoonForDatum();
        // StructureSpoon structure = ...;
        // this.graph = analyzer.analyzeDatumAndGetResult(structure);

        // Для примера создадим тестовый граф
//        this.graph = createTestGraph();
//        Path dirForProjectCopy = Paths.get("preproccesed_project");
//        System.out.println("Построение структуры проекта ...");
//        StructureSpoon structureSpoon = new StructureSpoon(dirForProjectCopy);
//
//        String inputDatumJson = "project_structure/data.json";
//        structureSpoon.initSpoon();
//        structureSpoon.initCpgGraph();
////
//        System.out.println("Анализ структуры проекта ...");
//        AnalyzerAfterSpoonForDatum analizerAfterSpoon = new AnalyzerAfterSpoonForDatum(inputDatumJson);
//        ResultGraph resultNode = analizerAfterSpoon.analyzeDatumAndGetResult(structureSpoon);
//        System.out.println(":::::::::::::::::::::::::::::::::::::::::");
//        resultNode.print();
//        System.out.println("Анализ структуры проекта завершён!");
//        this.graph = resultNode;
    }

    public ResultGraph getGraph() {

        return graph;
    }

//    public Map<String, Object> getNodeDetails(String nodeId) {
//        Map<String, Object> details = new HashMap<>();
//
//         node = graph.getNode(nodeId);
//        if (node == null) {
//            details.put("error", "Node not found");
//            return details;
//        }
//
//        details.put("node", node);
//
//        // Входящие рёбра
//        List<Edge> incoming = graph.getEdges().stream()
//                .filter(e -> e.getTo().equals(nodeId))
//                .collect(Collectors.toList());
//        details.put("incoming", incoming);
//
//        // Исходящие рёбра
//        List<Edge> outgoing = graph.getEdges().stream()
//                .filter(e -> e.getFrom().equals(nodeId))
//                .collect(Collectors.toList());
//        details.put("outgoing", outgoing);
//
//        return details;
//    }

//    public Map<String, Object> expandNode(String nodeId) {
//        Map<String, Object> result = new HashMap<>();
//        Set<String> connectedNodes = new HashSet<>();
//        List<ResultEdge> connectedEdges = new ArrayList<>();
//
//        // Находим соседей
//        for (ResultEdge edge : graph.getEdges()) {
//            if (edge.getFrom().equals(nodeId)) {
//                connectedNodes.add(edge.getTo());
//                connectedEdges.add(edge);
//            } else if (edge.getTo().equals(nodeId)) {
//                connectedNodes.add(edge.getFrom());
//                connectedEdges.add(edge);
//            }
//        }
//
//        // Добавляем текущий узел
//        connectedNodes.add(nodeId);
//
//        result.put("nodes", graph.getNodes().stream()
//                .filter(n -> connectedNodes.contains(n.getId()))
//                .collect(Collectors.toList()));
//        result.put("edges", connectedEdges);
//
//        return result;
//    }

//    public ResultGraph getSubgraph(String nodeId, int depth) {
//        ResultGraph subgraph = new ResultGraph();
//        Set<String> visited = new HashSet<>();
//        Queue<String> queue = new LinkedList<>();
//        Map<String, Integer> depths = new HashMap<>();
//
//        queue.add(nodeId);
//        depths.put(nodeId, 0);
//        visited.add(nodeId);
//
//        while (!queue.isEmpty()) {
//            String current = queue.poll();
//            int currentDepth = depths.get(current);
//
//            if (currentDepth >= depth) continue;
//
//            // Добавляем узел
//            Node node = graph.getNode(current);
//            if (node != null) {
//                subgraph.addNode(node);
//            }
//
//            // Находим исходящие рёбра
//            for (Edge edge : graph.getEdges()) {
//                if (edge.getFrom().equals(current) && !visited.contains(edge.getTo())) {
//                    visited.add(edge.getTo());
//                    depths.put(edge.getTo(), currentDepth + 1);
//                    queue.add(edge.getTo());
//
//                    Node targetNode = graph.getNode(edge.getTo());
//                    if (targetNode != null) {
//                        subgraph.addNode(targetNode);
//                    }
//                    subgraph.addEdge(edge);
//                }
//            }
//        }
//
//        return subgraph;
//    }
//
//    private ResultGraph createTestGraph() {
//        ResultGraph g = new ResultGraph();
//
//        // Создаём тестовые данные
//        g.addNode(new Node("1", "Game.main()", "METHOD", "CLASS", "com.example.Game.main"));
//        g.addNode(new Node("2", "Game.init()", "METHOD", "CLASS", "com.example.Game.init"));
//        g.addNode(new Node("3", "Executor.execute()", "INTERFACE_METHOD", "INTERFACE", "com.example.Executor.execute"));
//        g.addNode(new Node("4", "SyncExecutor.execute()", "METHOD", "CLASS", "com.example.SyncExecutor.execute"));
//        g.addNode(new Node("5", "AsyncExecutor.execute()", "METHOD", "CLASS", "com.example.AsyncExecutor.execute"));
//        g.addNode(new Node("6", "new SyncExecutor()", "CONSTRUCTOR", "CLASS", "com.example.SyncExecutor.SyncExecutor"));
//
//        g.addEdge(new Edge("1", "2", "CALLS", "[]", 10));
//        g.addEdge(new Edge("2", "3", "CALLS", "[]", 25));
//        g.addEdge(new Edge("3", "4", "IMPLEMENTS", "[]", -1));
//        g.addEdge(new Edge("3", "5", "IMPLEMENTS", "[]", -1));
//        g.addEdge(new Edge("2", "6", "CONSTRUCTOR_CALL", "[]", 30));
//
//        return g;
//    }
}
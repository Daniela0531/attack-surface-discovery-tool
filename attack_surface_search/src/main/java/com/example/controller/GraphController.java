package com.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.result_structure.ResultGraph;
import org.springframework.web.bind.annotation.*;
import com.example.service.GraphService;

@RestController
@RequestMapping("/")
public class GraphController {

    private final GraphService graphService;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public GraphController() throws Exception {
        this.graphService = new GraphService();
        // Запускаем анализ при старте
        graphService.analyzeProject();
    }

    /**
     * Получить весь граф
     */
    @GetMapping("/")
    public String getFullGraph() throws Exception {
        ResultGraph graph = graphService.getGraph();
        return gson.toJson(graph);
    }

//    /**
//     * Получить информацию об узле (при клике)
//     */
//    @GetMapping("/node/{nodeId}")
//    public String getNodeDetails(@PathVariable String nodeId) {
//        return gson.toJson(graphService.getNodeDetails(nodeId));
//    }

//    /**
//     * Раскрыть узел: получить соседей
//     */
//    @GetMapping("/expand/{nodeId}")
//    public String expandNode(@PathVariable String nodeId) {
//        return gson.toJson(graphService.expandNode(nodeId));
//    }

//    /**
//     * Получить подграф от указанного метода (анализ вызовов)
//     */
//    @GetMapping("/subgraph/{nodeId}")
//    public String getSubgraph(@PathVariable String nodeId,
//                              @RequestParam(defaultValue = "3") int depth) {
//        return gson.toJson(graphService.getSubgraph(nodeId, depth));
//    }
}

package com.example.all_traces;

import com.example.analizer.NewAnalyzer;
import com.example.analizer.followed_data.MethodArgument;
import com.example.result_structure.ResultGraph;
import com.example.structure.StructureSpoon;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnalyzeAllInputStructures {
    List<ResultGraph> allResults;
    public AnalyzeAllInputStructures() {
        this.allResults = new ArrayList<>();
    }
// для всех методов всех классов?
    public void buildAllTraces(StructureSpoon structureSpoon) {
        NewAnalyzer analyzer = new NewAnalyzer(structureSpoon);
        for (CtExecutable<?> executable : structureSpoon.getGraph().getNodes()) {
            if (executable instanceof CtMethod<?>) {
                for (CtParameter<?> parameter : executable.getParameters()) {
                    ResultGraph resultGraph = analyzer.analyzeDatumAndGetResult(new MethodArgument(parameter));
                    this.allResults.add(resultGraph);
                }
            }
        }

        int allEdges = 0;
        for (ResultGraph resultGraph : allResults) {
            allEdges += resultGraph.getEdges().size();
        }

        System.out.println("всего методов: " + allResults.size());
        System.out.println("всего переходов в другие методы: " + allEdges);
    }

    public void print() {
        for (ResultGraph resultGraph : allResults) {
            resultGraph.print();
        }
        System.out.println("всего методов: " + allResults.size());
    }

    public List<ResultGraph> getAllResults() {
        return allResults;
    }

}

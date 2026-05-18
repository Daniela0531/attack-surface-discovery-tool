package com.example.all_traces;

import com.example.analizer.NewAnalyzer;
import com.example.analizer.followed_data.MethodArgument;
import com.example.result_structure.ResultGraph;
import com.example.structure.StructureSpoon;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

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
//        getMethodArgumentFromDescription
        for (CtExecutable<?> executable : structureSpoon.getGraph().getNodes()) {
            if (executable instanceof CtMethod<?>) {
//                System.out.println(executable.getSignature());
                for (CtParameter<?> parameter : executable.getParameters()) {
                    ResultGraph resultGraph = analyzer.analyzeDatumAndGetResult(new MethodArgument(parameter));
                    this.allResults.add(resultGraph);
                }
            }
        }
    }

    public void print() {
        for (ResultGraph resultGraph : allResults) {
            resultGraph.print();
        }
        System.out.println("всего методов: " + allResults.size());
    }
}

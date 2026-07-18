package com.example.all_traces;

import com.example.analizer_trace.NewAnalyzer;
import com.example.analizer_trace.followed_data.MethodArgument;
import com.example.entry_points.EntryPoint;
import com.example.result_structure.ResultTrace;
import com.example.structure.StructureSpoon;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeAllInputStructures {
    List<ResultTrace> allResults;
    public AnalyzeAllInputStructures() {
        this.allResults = new ArrayList<>();
    }
// для всех методов всех классов?
    public void buildAllTraces(StructureSpoon structureSpoon) {
        NewAnalyzer analyzer = new NewAnalyzer(structureSpoon);
        for (CtExecutable<?> executable : structureSpoon.getGraph().getNodes()) {
            if (executable instanceof CtMethod<?>) {
                for (CtParameter<?> parameter : executable.getParameters()) {
                    ResultTrace resultTrace = analyzer.analyzeDatumAndGetResult(new MethodArgument(parameter));
                    this.allResults.add(resultTrace);
                }
            }
        }

        int allEdges = 0;
        for (ResultTrace resultTrace : allResults) {
            allEdges += resultTrace.getEdges().size();
        }

        System.out.println("всего методов: " + allResults.size());
        System.out.println("всего переходов в другие методы: " + allEdges);
    }

    public void buildTracesForEntryPoints(List<EntryPoint> allNodes, StructureSpoon structureSpoon) {
        NewAnalyzer analyzer = new NewAnalyzer(structureSpoon);
        for (EntryPoint entryPoint : allNodes) {
            if (entryPoint.getCtExecutable() instanceof CtMethod<?>) {

                ResultTrace resultTrace = analyzer.analyzeDatumAndGetResult(
                        new MethodArgument(
                                entryPoint.getCtExecutable().getParameters().get(entryPoint.getPositionInMethod())
                        )
                );
                this.allResults.add(resultTrace);

            }
        }

        int allEdges = 0;
        for (ResultTrace resultTrace : allResults) {
            allEdges += resultTrace.getEdges().size();
        }

        System.out.println("всего методов: " + allResults.size());
        System.out.println("всего переходов в другие методы: " + allEdges);
    }

    public void print() {
        for (ResultTrace resultTrace : allResults) {
            resultTrace.print();
        }
        System.out.println("всего методов: " + allResults.size());
    }

    public List<ResultTrace> getAllResults() {
        return allResults;
    }

}

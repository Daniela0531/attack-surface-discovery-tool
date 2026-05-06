package org.example.structure.graph;

import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.*;

import java.util.*;

public class CpgGraph {
    CtMethod<?> start = null;
    List<CtExecutable<?>> nodes = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    public void addNode(CtExecutable<?> node) {
        nodes.add(node);
    }

    public void addEdge(CtExecutable<?> from, CtExecutable<?> to, RelationType type,
                        CtExpression<?> callExpression, Label label, Condition condition) {
        edges.add(new Edge(from, to, type, callExpression, label, condition));
    }

    public void printStats() {
        System.out.println("Всего узлов: " + nodes.size());
        System.out.println("Всего ребер: " + edges.size());

        for (CtExecutable n : nodes) {
            System.out.println("\nNode :: " + n.getSignature() + "\nhas edges:::");
            for (Edge e : edges) {
                if (e.getLabel() != Label.UNKNOWN && Objects.equals(e.from, n)) {
                    if (e.getCallExpression() instanceof CtConstructorCall<?>) {
                        System.out.println("edje to constructor " + e.to.getSignature());
                    } else {
                        System.out.println("edje to method " + e.to.getSignature());
                    }
                }
            }
        }
        System.out.println("======================================");
    }

    public List<CtExecutable<?>> getNodes() {
        return this.nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setStart(CtMethod<?> startMethod) {
        this.start = startMethod;
    }
}


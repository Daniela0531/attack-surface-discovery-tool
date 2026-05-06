package org.example.structure.graph;

import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;

public class Node {
    CtExecutable<?> executable;
    NodeType type; // METHOD, CONSTRUCTOR


    public Node(CtExecutable<?> executable) {
        this.executable = executable;
        if (executable instanceof CtMethod<?>)
            this.type = NodeType.METHOD;
        else if (executable instanceof CtConstructor<?>)
            this.type = NodeType.CONSTRUCTOR;
        else {
            this.type = null;
            System.out.println("Неизвестный тип Node !!!!!!!!!");
        }
    }

    public CtExecutable<?> getExecutable() {
        return executable;
    }
    public NodeType getType() {
        return type;
    }
}

package org.example.analizer.structures;

public class Operation {
    OperationType type;
    String operationPerformer;
    public Operation(OperationType type , String operationPerformer) {
        this.operationPerformer = operationPerformer;
        this.type = type;
    }
}

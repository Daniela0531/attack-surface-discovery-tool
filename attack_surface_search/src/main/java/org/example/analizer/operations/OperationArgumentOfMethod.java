package org.example.analizer.operations;

import org.example.analizer.Method;

public class OperationArgumentOfMethod implements Operation {
    private String type;
    private String operationPerformerClass;
    private String operationPerformerName;
    private String operationSubjectName;
    private String content;
//    private String description;
    private Method method;
    private int positionNumber;

    public Method getMethod() {
        return method;
    }
    public void setMethod(Method method) {
        this.method = method;
    }

    public int getPositionNumber() {
        return this.positionNumber;
    }
    public void setPositionNumber(int positionNumber) {
        this.positionNumber = positionNumber;
    }
    public String getOperationPerformerClass() {
        return this.operationPerformerClass;
    }

    public void setOperationPerformerClass(String operationPerformerClass) {
        this.operationPerformerClass = operationPerformerClass;
    }
    public String getType() {
        return this.type;
    }
    public String getOperationPerformerName() {
        return this.operationPerformerName;
    }
    public String getOperationSubjectName() {
        return this.operationSubjectName;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setOperationPerformerName(String operationPerformerName) {
        this.operationPerformerName = operationPerformerName;
    }
    public void setOperationSubjectName(String operationSubjectName) {
        this.operationSubjectName = operationSubjectName;
    }
//    public Operation(OperationType type , String operationPerformer) {
//        this.operationPerformer = operationPerformer;
//        this.type = type;
//    }
}

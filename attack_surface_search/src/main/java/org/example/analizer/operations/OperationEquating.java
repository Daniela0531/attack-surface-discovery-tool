package org.example.analizer.operations;

// что потом надо будет распарсить:
// вызов как аргумент метода
// присваивание
// тернарный оператор
// арифметические операции
// если это метод: кто инициировал, описание метода, позиция аргумента
//
// что бывает:
// конструкторы
// вызовы методов
// вызов статического метода
// присваивание
// тернарный оператор
// арифметические операции с присваиванием
// циклы
// условия
// свитчи
//
public class OperationEquating implements Operation {
    private String type;
    private String operationPerformerName;
    private String operationSubjectName;

    private String operationPerformerClass;

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
//    private String content;
//    private String description;
//    public Operation(OperationType type , String operationPerformer) {
//        this.operationPerformer = operationPerformer;
//        this.type = type;
//    }
}

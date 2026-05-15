package com.example.analizer;

public class Method {
    private String name;
    private int numberOfArguments;

    public Method() {
        this.name = null;
        this.numberOfArguments = -1;
    }
    public Method(String name, int numberOfArguments) {
        this.name = name;
        this.numberOfArguments = numberOfArguments;
    }
    public String getName() {
        return name;
    }
    public int getNumberOfArguments() {
        return numberOfArguments;
    }

    public void setName(String methodName) {
        this.name = methodName;
    }

    public void setNumberOfArguments(int numberOfArguments) {
        this.numberOfArguments = numberOfArguments;
    }
}

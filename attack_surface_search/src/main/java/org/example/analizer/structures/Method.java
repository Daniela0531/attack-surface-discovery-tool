package org.example.analizer.structures;

public class Method {
    private String name;
    private int numberOfArguments;

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
}

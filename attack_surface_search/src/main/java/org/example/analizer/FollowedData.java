package org.example.analizer;

import java.util.ArrayList;

public class FollowedData {
    private String javaClass = "";
    private String method;
    private String data;
    private ArrayList<String> operations = new ArrayList<>();

    public FollowedData(String javaClass , String method , String data) {
        this.javaClass = javaClass;
        this.method = method;
        this.data = data;
        this.operations = new ArrayList<>();
    }

    public FollowedData() {
        this.operations = new ArrayList<>();
    }

    public String getJavaClass() {
        return javaClass;
    }
    public String getMethod() {
        return method;
    }
    public String getData() {
        return data;
    }

    public ArrayList<String> getOperations() {
        return operations;
    }

    public int getOperationsCount() {
        return operations.size();
    }

    public void pushOperation(String operation) {
        operations.add(operation);
    }
}

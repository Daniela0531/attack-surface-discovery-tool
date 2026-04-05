package org.example.analizer.structures;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class FollowedData {
    @JsonProperty("package")
    private String javaPackage = "";
    @JsonProperty("class")
    private String javaClass = "";
    private String file = "";
    private Method method;
    private String data;
    private ArrayList<String> operations = new ArrayList<>();
    private boolean isAlreadyAnalized = false;

    public FollowedData(String file, String javaPackage, String javaClass, Method method, String data) {
        this.javaPackage = javaPackage;
        this.javaClass = javaClass;
        this.file = file;
        this.method = method;
        this.data = data;
        this.operations = new ArrayList<>();
    }

    public FollowedData() {
        this.method = null;
        this.operations = new ArrayList<>();
    }

    public String getJavaClass() {
        return javaClass;
    }
    public Method getMethod() {
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
    public boolean isAlreadyAnalized() {
        return isAlreadyAnalized;
    }
    public void setAlreadyAnalized() {
        this.isAlreadyAnalized = true;
    }

    public String getFile() {
        return file;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    public void setMehod(Method method) {
        this.method = method;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }
    public void setFile(String file) {
        this.file = file;
    }
}

package org.example.analizer.followed_data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.analizer.Method;

import java.util.ArrayList;

public class FollowedData {
    @JsonProperty("package")
    private String javaPackage = "";
    @JsonProperty("class")
    private String javaClass = "";
    private String file = "";
    // нумерация позиции с нуля
    private String name;
    @JsonProperty("positionInMethod")
    private int positionInMethod = -1;
    @JsonProperty("method")
    private Method method;
    private ArrayList<String> operations = new ArrayList<>();
    private boolean isAlreadyAnalized = false;

    public FollowedData(String file, String javaPackage, String javaClass, Method method) {
        this.javaPackage = javaPackage;
        this.javaClass = javaClass;
        this.file = file;
        this.method = method;
        this.name = null;
        this.operations = new ArrayList<>();
        this.positionInMethod = -1;
    }
    public FollowedData(String file, String javaPackage, String javaClass, Method method, String data) {
        this.javaPackage = javaPackage;
        this.javaClass = javaClass;
        this.file = file;
        this.method = method;
        this.name = data;
        this.operations = new ArrayList<>();
        this.positionInMethod = -1;
    }

    public FollowedData() {
        this.method = null;
        this.operations = new ArrayList<>();
    }
    public int getPositionInMethod() {
        return (positionInMethod == -1)?null:positionInMethod;
    }
    public void setPositionInMethod(int p) {
        this.positionInMethod = p;
    }

    public String getJavaClass() {
        return javaClass;
    }
    public Method getMethod() {
        return method;
    }
    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }
    public void print() {
        System.out.println("data :::");
        System.out.println("package = " + javaPackage);
        System.out.println("class = " + javaClass);
        System.out.println("name = " + name);
        System.out.println("method = " + method.getName());
        System.out.println("positionInMethod = " + positionInMethod);
    }
}


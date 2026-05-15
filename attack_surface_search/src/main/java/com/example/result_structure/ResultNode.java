package com.example.result_structure;

import com.example.analizer.followed_data.location.FollowedDatumLocation;

import java.util.ArrayList;

public class ResultNode {
    public String methodName;
    private FollowedDatumLocation location;
    private ArrayList<ResultNode> operations;
//    private ResultStructureNode child;

    public ResultNode() {
        this.operations = new ArrayList<>();
//        this.positionInMethod = -1;
//        this.method = new Method();
    }
    public ResultNode(FollowedDatumLocation location) {
        this.operations = new ArrayList<>();
        this.location = location;
//        this.child = null;
    }

//    public String getMethodName() {
//        return this.method.getName();
//    }
    public void addOperation(ResultNode child) {
        operations.add(child);
    }

//    public void addChild(ResultStructureNode child) {
//        this.child = child;
//    }

    public ArrayList<ResultNode> getOperations() {
        return operations;
    }

    public void print(int i) {
        String tabs = "   ".repeat(i);
//        System.out.println(tabs + "package ::" + this.javaPackage);
//        System.out.println(tabs + "class ::" + this.javaClass);
//        System.out.println(tabs + "method.getName ::" + this.method.getName());
//        System.out.println(tabs + "method.getArgsCount ::" + this.method.getNumberOfArguments());
//        System.out.println(tabs + "positionInMethod ::" + this.positionInMethod);

        System.out.println(i + ":::");
        if (location != null)
            location.print(i);
        for (ResultNode child : operations) {
            child.print(i);
        }
//        if (child != null)
//            child.print(i + 1);
    }

    public void setOperations(ArrayList<ResultNode> operations) {
        this.operations = operations;
    }

//    public ResultStructureNode getChild() {
//        return child;
//    }
//    public void setPositionInMethod(int positionInMethod) {
//        this.positionInMethod = positionInMethod;
//    }
//    public void setMethod(String methodName) {
//        this.method.setName(methodName);
//    }

//    public void setMethod(String methodName, int numberOfArguments) {
//        this.method.setName(methodName);
//        this.method.setNumberOfArguments(numberOfArguments);
//    }
//
//    public void setJavaPackage(String javaPackage) {
//        this.javaPackage = javaPackage;
//    }
//    public void setJavaClass(String javaClass) {
//        this.javaClass = javaClass;
//    }
}

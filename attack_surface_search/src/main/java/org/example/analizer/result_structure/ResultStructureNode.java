package org.example.analizer.result_structure;

import org.example.analizer.Method;
import org.example.analizer.followed_data.FollowedDataLocation;

import java.util.ArrayList;

public class ResultStructureNode {
    private FollowedDataLocation location;
    private ArrayList<ResultStructureNode> children;

    public ResultStructureNode() {
        this.children = new ArrayList<>();
//        this.positionInMethod = -1;
//        this.method = new Method();
    }
    public ResultStructureNode(FollowedDataLocation location) {
        this.children = new ArrayList<>();
        this.location = location;
    }

//    public String getMethodName() {
//        return this.method.getName();
//    }
    public void addChild(ResultStructureNode child) {
        children.add(child);
    }

    public ArrayList<ResultStructureNode> getChildren() {
        return children;
    }

    public void print(int i) {
//        String tabs = "   ".repeat(i);
//        System.out.println(tabs + "package ::" + this.javaPackage);
//        System.out.println(tabs + "class ::" + this.javaClass);
//        System.out.println(tabs + "method.getName ::" + this.method.getName());
//        System.out.println(tabs + "method.getArgsCount ::" + this.method.getNumberOfArguments());
//        System.out.println(tabs + "positionInMethod ::" + this.positionInMethod);

        System.out.println(i + ":::");
        location.print();
        for (ResultStructureNode child : children) {
            child.print(i + 1);
        }
    }

    public void setChildren(ArrayList<ResultStructureNode> children) {
        this.children = children;
    }
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

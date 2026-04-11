package org.example.analizer.result_structure;

import org.example.analizer.Method;

import java.util.ArrayList;

public class ResultStructureNode {
//    private String type;
//    private Path path;
//    private String name;
    private int positionInMethod = -1;
    private Method method;
    private ArrayList<ResultStructureNode> children;
//    private ResultStructureNode parent;
//    private String[] content;
//    private int level;

//    private boolean isAlreadyAnalized = false;
    public ResultStructureNode() {
//        this.path = null;
        this.children = new ArrayList<>();
//        this.parent = null;
//        this.type = null;
//        this.level = 0;
//        this.name = null;
        this.positionInMethod = -1;
        this.method = new Method();
//        this.isAlreadyAnalized = false;
//        this.content = null;
    }
    public ResultStructureNode(String methodName, int positionInMethod) {
//        this.path = null;
        this.children = new ArrayList<>();
//        this.parent = null;
//        this.type = null;
//        this.level = 0;
//        this.name = null;
        this.positionInMethod = positionInMethod;
        this.method = new Method();
        this.method.setName(methodName);
//        this.isAlreadyAnalized = false;
//        this.content = null;
    }
//    public String[] getContent() {
//        return this.content;
//    }
    public String getMethodName() {
        return this.method.getName();
    }
    public void addChild(ResultStructureNode child) {
        children.add(child);
    }

    public ArrayList<ResultStructureNode> getChildren() {
        return children;
    }
//    public void setPath(Path path) {
//        this.path = path;
//    }
//    public void setType(String type) {
//        this.type = type;
//    }
//    public void setParent(org.example.analizer.project_structure.ProjectStructureNode parent) {
//        this.parent = parent;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public String getName() {
//        return this.name;
//    }

    public void print(int i) {
        String tabs = "   ".repeat(i);
        System.out.println(tabs + "method.getName ::" + this.method.getName());
        System.out.println(tabs + "positionInMethod ::" + this.positionInMethod);
//        System.out.println(tabs + path);

        for (ResultStructureNode child : children) {
            child.print(i + 1);
        }
    }

//    public String getType() {
//        return this.type;
//    }

//    public Path getPath() {
//        return this.path;
//    }

    public void setChildren(ArrayList<ResultStructureNode> children) {
        this.children = children;
    }
    public void setPositionInMethod(int positionInMethod) {
        this.positionInMethod = positionInMethod;
    }
    public void setMethod(String methodName) {
        this.method.setName(methodName);
    }

    public void setMethod(String methodName, int numberOfArguments) {
        this.method.setName(methodName);
        this.method.setNumberOfArguments(numberOfArguments);
    }

//    public int getLevel() {
//        return  level;
//    }

//    public void setLevel(int level) {
//        this.level = level;
//    }

//    public ResultStructureNode getParent() {
//        return parent;
//    }
}

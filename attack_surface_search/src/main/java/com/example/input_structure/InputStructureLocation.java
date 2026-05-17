package com.example.input_structure;

import com.example.analizer.Method;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InputStructureLocation {
    @JsonProperty("package")
    private String javaPackage = "";
    @JsonProperty("class")
    private String javaClass = "";
    @JsonProperty("positionInMethod")
    private int positionInMethod = 0;
    @JsonProperty("method")
    private Method method;

//    private List<StartMethodArgument> methodArguments;
    public InputStructureLocation() {
//        this.javaPackage = "";
//        this.javaClass = "";
        this.method = new Method();
    }
    public InputStructureLocation(String javaPackage, String javaClass, Method method, int positionInMethod) {
        this.javaPackage = javaPackage;
        this.javaClass = javaClass;
        this.method = method;
        this.positionInMethod = positionInMethod;
    }
    public void print(int i) {
        String tabs = "   ".repeat(i);
        System.out.println(tabs + i + ":::\n" + tabs + "type :: argument of method");
        System.out.println(tabs + "package = " + javaPackage);
        System.out.println(tabs + "class = " + javaClass);
        System.out.println(tabs + "method = " + method.getName());
        System.out.println(tabs + "positionInMethod = " + positionInMethod);
    }

    public int getPositionInMethod() {
        return positionInMethod;
    }

    public String getJavaClass() {
        return javaClass;
    }

    public Method getMethod() {
        return method;
    }

//    public InputStructureMethod getMethodStructure() {
//        InputStructureMethod inputStructureMethod = new InputStructureMethod(javaPackage, javaClass, method.getName(), );
//    }
}

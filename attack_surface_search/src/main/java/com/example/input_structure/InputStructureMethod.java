package com.example.input_structure;

import com.example.analizer_trace.Method;
import com.example.analizer_trace.followed_data.location.MethodLocation;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class InputStructureMethod {
    @JsonProperty("package")
    private String packageName = "";
    @JsonProperty("class")
    private String className = "";
    @JsonProperty("methodName")
    private String methodName;
    @JsonProperty("methodArguments")
    private List<InputStructureMethodArgument> methodArguments;

//    private List<StartMethodArgument> methodArguments;
    public InputStructureMethod() {
        this.packageName = "";
        this.className = "";
        this.methodName = "";
        this.methodArguments = new ArrayList<>();
    }

    public InputStructureMethod(String packageName, String className, String methodName, List<InputStructureMethodArgument> methodArguments) {
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.methodArguments = methodArguments;
    }

    public String getPackageName() {
        return packageName;
    }
    public String getClassName() {
        return className;
    }
    public String getMethodName() {
        return methodName;
    }
    public List<InputStructureMethodArgument> getMethodArguments() {
        return methodArguments;
    }

    public void print() {
        System.out.println("StartMethod :::");
        System.out.println(
                " package = " + packageName +
                "\n class = " + className +
                "\n methodName = " + methodName +
                "\n methodArguments : "
        );
        for (InputStructureMethodArgument arg : methodArguments) {
            System.out.println(
                    "\n     type : " + arg.getType() +
                    "\n     name : " + arg.getName() + "\n"
            );
        }
    }

    public MethodLocation getLocation() {
        return new MethodLocation(packageName, className, new Method(methodName, methodArguments.size()), 0);
    }
}

package org.example.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.analizer.Method;

import java.util.ArrayList;
import java.util.List;

public class StartMethod {
    @JsonProperty("package")
    private String packageName = "";
    @JsonProperty("class")
    private String className = "";
    @JsonProperty("methodName")
    private String methodName;
    @JsonProperty("methodArguments")
    private List<StartMethodArgument> methodArguments;

//    private List<StartMethodArgument> methodArguments;
    public StartMethod() {
        this.packageName = "";
        this.className = "";
        this.methodName = "";
        this.methodArguments = new ArrayList<>();
    }

    public StartMethod(String packageName, String className, String methodName, List<StartMethodArgument> methodArguments) {
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
    public List<StartMethodArgument> getMethodArguments() {
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
        for (StartMethodArgument arg : methodArguments) {
            System.out.println(
                    "\n     type : " + arg.getType() +
                    "\n     name : " + arg.getName() + "\n"
            );
        }
    }
}

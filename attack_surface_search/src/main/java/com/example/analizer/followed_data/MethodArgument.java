package com.example.analizer.followed_data;

import com.example.analizer.followed_data.location.FollowedDatumLocation;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

public class MethodArgument implements FollowedDatumInMethodContext {
    private FollowedDatumLocation location;
//    private boolean neededToAnalyze;
//    private OperationType operationType;
    private CtParameter<?> parameter;
    private String parameterImplName;
    private CtExecutable<?> executable;
    private CtExecutable<?> parent;

//    public MethodArgument() {
//    }
//    public MethodArgument(FollowedDatumLocation location) {
//        this.location = location;
//    }


    public MethodArgument(CtParameter<?> parameter) {
//        this.location = location;
        this.parameter = parameter;
        CtExecutable<?> executable = parameter.getParent(CtExecutable.class);
        if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
            this.executable = executable;
        } else {
            System.out.println("MethodArgument инициировали аргументом не метода и не конструктора");
            this.executable = null;
        }
    }

//    public MethodArgument(CtParameter<?> parameter, String parameterImplName) {
////        this.location = location;
//        this.parameterImplName = parameterImplName;
//        this.parameter = parameter;
//        CtExecutable<?> executable = parameter.getParent(CtExecutable.class);
//        if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
//            this.executable = executable;
//        } else {
//            System.out.println("MethodArgument инициировали аргументом не метода и не конструктора");
//            this.executable = null;
//        }
//    }

    public MethodArgument(CtParameter<?> parameter, String parameterImplName, CtExecutable<?> parent) {
//        this.location = location;
        this.parent = parent;
        this.parameterImplName = parameterImplName;
        this.parameter = parameter;
        CtExecutable<?> executable = parameter.getParent(CtExecutable.class);
        if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
            this.executable = executable;
        } else {
            System.out.println("MethodArgument инициировали аргументом не метода и не конструктора");
            this.executable = null;
        }
    }

//    public void setParameterImplName(String parameterImplName) {
//        this.parameterImplName = parameterImplName;
//    }

    @Override
    public CtExecutable<?> getLocation() {
        return executable;
    }
    @Override
    public void setLocation(CtExecutable<?> location) {
        this.executable = location;
    }
//    @Override
//    public LocationType getLocationType() {
//        return location.getType();
//    }
    @Override
    public boolean isEquals(FollowedDatum datum) {
        if (!(datum instanceof MethodArgument)) {
            return false;
        }
        return ((MethodArgument) datum).getLocation() == getLocation() &&
                ((MethodArgument) datum).getParameterImplName().equals(getParameterImplName()) &&
                ((MethodArgument) datum).getParent() == getParent();
    }

    @Override
    public String getName() {
        return parameter.getSimpleName();
    }

    @Override
    public MethodArgument get() {
        return null;
    }

    public CtParameter<?> getParameter() {
        return parameter;
    }
    public String getParameterImplName() {
        return parameterImplName;
    }
    public CtExecutable<?> getParent() {
        return parent;
    }
}


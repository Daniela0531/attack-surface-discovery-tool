package com.example.analizer_trace.followed_data;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;

public class ClassField implements FollowedDatum {
    private CtClass<?> location;
//    private boolean neededToAnalyze;
//    private OperationType operationType;
    private CtField<?> field;

    public ClassField() {
    }

    public ClassField(CtField<?> field) {
        this.location = field.getParent(CtClass.class);
        this.field = field;
    }


    public CtClass<?> getLocation() {
        return location;
    }
    public CtField<?> getField() {
        return field;
    }

    public void setLocation(CtClass<?> location) {
        this.location = location;
    }

    @Override
    public boolean isEquals(FollowedDatum datum) {
        if (!(datum instanceof ClassField)) {
            return false;
        }
        return ((ClassField) datum).getField() == field;
    }

    @Override
    public String getName() {
        return field.getSimpleName();
    }

    @Override
    public String getMethodName() {
        return null;
    }

//    public String getAssignmentParam() {
//        return
//    }
}


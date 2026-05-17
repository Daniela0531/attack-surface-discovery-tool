package com.example.analizer.followed_data;

import com.example.analizer.followed_data.location.FollowedDatumLocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;

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

}


package com.example.analizer_trace.followed_data;

import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.*;

public class LocalVariableInMethod implements FollowedDatumInMethodContext {
//    private FollowedDatumLocation location;
//    private boolean neededToAnalyze;
//    private OperationType operationType;
//    private CtExpression<?> expression;
    private CtLocalVariable<?> variableAccess;
//    private
    private CtExecutable<?> executable;
    private boolean isOutsideMethod = false;

    public Boolean isOutsideLib() {
        return isOutsideMethod;
    }

    public LocalVariableInMethod(CtLocalVariable<?> variableAccess) {
//        this.location = location;
        this.variableAccess = variableAccess;
//        this.expression = expression;
        CtExecutable<?> executable = variableAccess.getParent(CtExecutable.class);
        if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
            this.executable = executable;
            if (executable.getReference().getDeclaration() == null || executable.getReference().getDeclaration().getBody() == null) {
                this.isOutsideMethod = true;
            }
        } else {
//            System.out.println("VariableAccessInMethod инициировали выражением не метода и не конструктора");
            this.executable = null;
            this.isOutsideMethod = false;
        }
    }

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
        if (!(datum instanceof LocalVariableInMethod)) {
            return false;
        }
        return ((LocalVariableInMethod) datum).getLocation() == getLocation() &&
                ((LocalVariableInMethod) datum).getVariableAccess() == getVariableAccess();
    }

    @Override
    public String getName() {
        return variableAccess.getSimpleName();
    }

    @Override
    public String getMethodName() {
        return executable.getSignature();
    }

    @Override
    public FollowedDatumInMethodContext get() {
        return null;
    }

    public CtLocalVariable<?> getVariableAccess() {
        return variableAccess;
    }

    public String getAssignmentParam() {
        return variableAccess.getDefaultExpression().toString();
    }
}


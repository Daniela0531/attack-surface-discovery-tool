package com.example.analizer.followed_data.operation;

import com.example.analizer.followed_data.FollowedDatum;
import com.example.analizer.followed_data.FollowedDatumInMethodContext;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;

public class AssignmentInMethod implements FollowedDatumInMethodContext {
//    private FollowedDatumLocation location;
//    private boolean neededToAnalyze;
//    private OperationType operationType;
//    private CtExpression<?> expression;
    private CtVariableWrite<?> variableAccess;
    private CtAssignment<?,?> assignment;
//    private
    private CtExecutable<?> executable;

//    public CtAssignmentInMethod() {
//    }

    public AssignmentInMethod(CtVariableWrite<?> variableAccess) {
//        this.location = location;
        this.variableAccess = variableAccess;
        this.assignment = (CtAssignment<?, ?>) variableAccess.getParent();
//        this.expression = expression;
        CtExecutable<?> executable = assignment.getParent(CtExecutable.class);
        if (executable instanceof CtMethod<?> || executable instanceof CtConstructor<?>) {
            this.executable = executable;
        } else {
            System.out.println("VariableAccessInMethod инициировали выражением не метода и не конструктора");
            this.executable = null;
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
        if (!(datum instanceof AssignmentInMethod)) {
            return false;
        }
        return ((AssignmentInMethod) datum).getLocation() == getLocation() &&
                ((AssignmentInMethod) datum).getVariableAccess() == getVariableAccess() &&
                ((AssignmentInMethod) datum).assignment == assignment;
    }
    public CtAssignment<?,?> getAssignment() {
        return assignment;
    }

    @Override
    public String getName() {
        return variableAccess.getVariable().getSimpleName();
    }

    @Override
    public FollowedDatumInMethodContext get() {
        return null;
    }

    public CtVariableWrite<?> getVariableAccess() {
        return variableAccess;
    }
}


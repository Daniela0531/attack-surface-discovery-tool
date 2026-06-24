package com.example.analizer_trace.followed_data;

import spoon.reflect.code.*;
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
//            System.out.println("VariableAccessInMethod инициировали выражением не метода и не конструктора");
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

    public String getAssignmentParam() {
        String assignedName = "";
        CtExpression<?> leftHandSide = assignment.getAssigned();
        if (leftHandSide instanceof CtVariableWrite) {
            // Присваивание в локальную переменную, поле класса или параметр метода
            CtVariableWrite<?> variableWrite = (CtVariableWrite<?>) leftHandSide;
            assignedName = variableWrite.getVariable().getSimpleName();

        } else if (leftHandSide instanceof CtFieldWrite) {
            // Явное присваивание в поле (например, this.myField = 10)
            CtFieldWrite<?> fieldWrite = (CtFieldWrite<?>) leftHandSide;
            assignedName = fieldWrite.getVariable().getSimpleName();
        }
        return assignedName;
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


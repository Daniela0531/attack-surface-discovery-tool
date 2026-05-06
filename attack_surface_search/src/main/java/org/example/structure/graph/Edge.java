package org.example.structure.graph;

import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.CtExecutable;

public class Edge {
    CtExecutable<?> from;
    CtExecutable<?> to;
    RelationType type; // CONTAINS, CALLS
    CtExpression<?> callExpression; // Аргументы вызова (для CALLS) в формате JSON массива
//    Map<CtExpression<?>, OriginalSourceForArg> originalToFinalArguments = new HashMap<>();
    Condition condition; //
    Label label;

    public Edge(CtExecutable<?> from, CtExecutable<?> to, RelationType type,
                CtExpression<?> callExpression, Label label, Condition condition) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.callExpression = callExpression;
        this.label = label;
        this.condition = condition;
//        this.originalToFinalArguments = new HashMap<>();
    }

    public Label getLabel() {
        return this.label;
    }

//    public void setOriginalForFinalArguments(CtExpression<?> arg, OriginalSourceForArg originalSource) {
//        this.originalToFinalArguments.put(arg, originalSource);
//    }

    public CtExpression<?> getCallExpression() {
        return callExpression;
    }
    public CtExecutable<?> getFrom() {
        return from;
    }
    public CtExecutable<?> getTo() {
        return to;
    }
}


///**
// * Класс-обертка для унифицированной работы с разными типами вызовов в Spoon.
// */
//class CallExpression {
//    private final Object value;
//    private final CallType type;
//
//    // Приватный конструктор
//    private CallExpression(Object value, CallType type) {
//        this.value = value;
//        this.type = type;
//    }
//
//    // ==================== ФАБРИЧНЫЕ МЕТОДЫ ====================
//
//    public static CallExpression of(CtInvocation<?> invocation) {
//        return new CallExpression(invocation, CallType.INVOCATION);
//    }
//
//    public static CallExpression of(CtConstructorCall<?> constructorCall) {
//        return new CallExpression(constructorCall, CallType.CONSTRUCTOR_CALL);
//    }
//
//    public static CallExpression of(CtLambda<?> lambda) {
//        return new CallExpression(lambda, CallType.LAMBDA);
//    }
//
//    public static CallExpression of(CtExecutableReferenceExpression<?, ?> ref) {
//        return new CallExpression(ref, CallType.EXECUTABLE_REF);
//    }
//
//    public static CallExpression of(CtTypeAccess<?> typeAccess) {
//        return new CallExpression(typeAccess, CallType.TYPE_ACCESS);
//    }
//
//    // ==================== МЕТОДЫ ПРОВЕРКИ ТИПА ====================
//
//    public boolean isInvocation() { return type == CallType.INVOCATION; }
//    public boolean isConstructorCall() { return type == CallType.CONSTRUCTOR_CALL; }
//    public boolean isLambda() { return type == CallType.LAMBDA; }
//    public boolean isExecutableRef() { return type == CallType.EXECUTABLE_REF; }
//    public boolean isTypeAccess() { return type == CallType.TYPE_ACCESS; }
//
//    public CallType getType() { return type; }
//
//    // ==================== МЕТОДЫ ПОЛУЧЕНИЯ ЗНАЧЕНИЙ ====================
//
//    public CtInvocation<?> asInvocation() {
//        checkType(CallType.INVOCATION);
//        return (CtInvocation<?>) value;
//    }
//
//    public CtConstructorCall<?> asConstructorCall() {
//        checkType(CallType.CONSTRUCTOR_CALL);
//        return (CtConstructorCall<?>) value;
//    }
//
//    public CtLambda<?> asLambda() {
//        checkType(CallType.LAMBDA);
//        return (CtLambda<?>) value;
//    }
//
//    public CtExecutableReferenceExpression<?, ?> asExecutableRef() {
//        checkType(CallType.EXECUTABLE_REF);
//        return (CtExecutableReferenceExpression<?, ?>) value;
//    }
//
//    public CtTypeAccess<?> asTypeAccess() {
//        checkType(CallType.TYPE_ACCESS);
//        return (CtTypeAccess<?>) value;
//    }
//
//    private void checkType(CallType expected) {
//        if (type != expected) {
//            throw new IllegalStateException(
//                    "Expected " + expected + " but was " + type);
//        }
//    }
//
//    // ==================== ОБЩИЕ МЕТОДЫ ====================
//
//    public String getTargetName() {
//        switch (type) {
//            case INVOCATION:
//                CtInvocation<?> inv = (CtInvocation<?>) value;
//                return inv.getExecutable().getSimpleName();
//
//            case CONSTRUCTOR_CALL:
//                CtConstructorCall<?> constr = (CtConstructorCall<?>) value;
//                return "new " + constr.getExecutable()
//                        .getDeclaringType().getSimpleName();
//
//            case LAMBDA:
//                CtLambda<?> lambda = (CtLambda<?>) value;
//                return "lambda$" + lambda.getSimpleName();
//
//            case EXECUTABLE_REF:
//                CtExecutableReferenceExpression<?, ?> ref =
//                        (CtExecutableReferenceExpression<?, ?>) value;
//                return ref.getExecutable().getSimpleName();
//
//            case TYPE_ACCESS:
//                CtTypeAccess<?> access = (CtTypeAccess<?>) value;
//                return access.getAccessedType().getSimpleName() + ".class";
//
//            default:
//                return "unknown";
//        }
//    }
//
//    public List<String> getArguments() {
//        switch (type) {
//            case INVOCATION:
//                return ((CtInvocation<?>) value).getArguments().stream()
//                        .map(Object::toString)
//                        .collect(Collectors.toList());
//
//            case CONSTRUCTOR_CALL:
//                return ((CtConstructorCall<?>) value).getArguments().stream()
//                        .map(Object::toString)
//                        .collect(Collectors.toList());
//
//            case LAMBDA:
//                return ((CtLambda<?>) value).getParameters().stream()
//                        .map(p -> p.getSimpleName())
//                        .collect(Collectors.toList());
//
//            default:
//                return new ArrayList<>();
//        }
//    }
//
//    public String getArgumentsAsJson() {
//        List<String> args = getArguments();
//        if (args.isEmpty()) return "[]";
//        return "[" + args.stream()
//                .map(arg -> "\"" + arg.replace("\"", "\\\"") + "\"")
//                .collect(Collectors.joining(", ")) + "]";
//    }
//
//    public int getLine() {
//        CtElement element = null;
//        switch (type) {
//            case INVOCATION: element = (CtInvocation<?>) value; break;
//            case CONSTRUCTOR_CALL: element = (CtConstructorCall<?>) value; break;
//            case LAMBDA: element = (CtLambda<?>) value; break;
//            case EXECUTABLE_REF:
//                element = (CtExecutableReferenceExpression<?, ?>) value; break;
//            case TYPE_ACCESS: element = (CtTypeAccess<?>) value; break;
//        }
//        return element != null && element.getPosition() != null ?
//                element.getPosition().getLine() : -1;
//    }
//
//    public String getCallTypeName() {
//        return type.getDisplayName();
//    }
//
//    // ==================== ENUM ДЛЯ ТИПОВ ====================
//
//    public enum CallType {
//        INVOCATION("METHOD_CALL"),
//        CONSTRUCTOR_CALL("CONSTRUCTOR_CALL"),
//        LAMBDA("LAMBDA"),
//        EXECUTABLE_REF("METHOD_REFERENCE"),
//        TYPE_ACCESS("TYPE_ACCESS");
//
//        private final String displayName;
//
//        CallType(String displayName) {
//            this.displayName = displayName;
//        }
//
//        public String getDisplayName() {
//            return displayName;
//        }
//    }
//}
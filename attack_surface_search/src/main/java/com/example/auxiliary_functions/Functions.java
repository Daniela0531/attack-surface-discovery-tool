package com.example.auxiliary_functions;

import com.example.analizer.Method;
import com.example.analizer.followed_data.FollowedDatum;
import com.example.analizer.followed_data.MethodArgument;
import com.example.analizer.followed_data.location.MethodLocation;
import com.example.structure.graph.Condition;
import com.example.structure.graph.Edge;
import com.example.structure.graph.Label;
import com.example.structure.graph.RelationType;
import com.example.input_structure.InputStructureMethod;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;
import java.util.stream.Collectors;

public class Functions {
    public boolean checkIsMethodMatchesStructure(CtMethod<?> ctMethod, InputStructureMethod inputStructureMethod) {
        return ctMethod.getSimpleName().equals(inputStructureMethod.getMethodName()) &&
                ctMethod.getParameters().size() == inputStructureMethod.getMethodArguments().size() &&
                ctMethod.getDeclaringType().getSimpleName().equals(inputStructureMethod.getClassName());
    }

    public boolean checkIsConstructorMatchesStructure(CtConstructor<?> ctConstructor, InputStructureMethod inputStructureMethod) {
        return ctConstructor.getDeclaringType().getSimpleName().equals(inputStructureMethod.getMethodName()) &&
                ctConstructor.getParameters().size() == inputStructureMethod.getMethodArguments().size() &&
                ctConstructor.getDeclaringType().getSimpleName().equals(inputStructureMethod.getClassName());
    }

    public List<Edge> getAllMethodCallsEdges(CtExecutable<?> node) {
        List<Edge> calleeMethods = new ArrayList<>();
        List<CtInvocation<?>> invocations = node.getElements(
                new TypeFilter<>(CtInvocation.class));

        for (CtInvocation<?> invocation : invocations) {
            CtExecutableReference<?> executableRef = invocation.getExecutable();
            if (executableRef == null) continue;
            // Пытаемся получить CtMethod
            CtExecutable<?> execDeclaration = executableRef.getExecutableDeclaration();

            if (execDeclaration instanceof CtMethod) {
                CtMethod<?> calleeMethod = (CtMethod<?>) execDeclaration;
                calleeMethods.add(
                        new Edge(
                                node,
                                calleeMethod,
                                RelationType.CALLS,
                                invocation,
                                Label.KNOWN,
                                Condition.ALWAYS
                        )
                );
            } else if (execDeclaration == null) {
                // Метод не разрешен — используем информацию из референса
                String methodName = executableRef.getSimpleName();
                String ownerType = executableRef.getDeclaringType() != null ?
                        executableRef.getDeclaringType().getQualifiedName() : "Unknown";

                calleeMethods.add(
                        new Edge(
                                node,
                                null,
                                RelationType.CALLS,
                                invocation,
                                Label.UNKNOWN,
                                Condition.ALWAYS
                        )
                );
            }
        }
        return calleeMethods;

    }

    public List<Edge> getAllInterfaceMethodCallsEdges(CtExecutable<?> node, Map<CtMethod<?>, List<CtMethod<?>>> implementationMap) {
        List<Edge> calleeMethods = new ArrayList<>();
        List<CtInvocation<?>> invocations = node.getElements(
                new TypeFilter<>(CtInvocation.class));

        for (CtInvocation<?> invocation : invocations) {
            CtExecutableReference<?> executableRef = invocation.getExecutable();
            if (executableRef == null) continue;
            // Пытаемся получить CtMethod
            CtExecutable<?> execDeclaration = executableRef.getExecutableDeclaration();

            if (execDeclaration instanceof CtMethod &&
                    ((CtMethod<?>) execDeclaration).getDeclaringType() instanceof CtInterface<?>) {
                CtMethod<?> calleeMethod = (CtMethod<?>) execDeclaration;
                for (CtMethod<?> methodImpl : implementationMap.get(calleeMethod)) {
                    calleeMethods.add(
                            new Edge(
                                    node,
                                    methodImpl,
                                    RelationType.CALLS,
                                    invocation,
                                    Label.KNOWN,
                                    Condition.ALWAYS
                            )
                    );
                }
            }
        }
        return calleeMethods;

    }

    // TODO возвращать место вызова + структуру, а не ребро
    public List<Edge> findAllConstructorsCalls(CtExecutable<?> node) {
        List<Edge> calleeConstructors = new ArrayList<>();
        List<CtConstructorCall<?>> ctConstructorCalls = node.getElements(
                new TypeFilter<>(CtConstructorCall.class));

        for (CtConstructorCall<?> constructorCall : ctConstructorCalls) {
            CtExecutableReference<?> executableRef = constructorCall.getExecutable();
            if (executableRef == null) continue;
            // Пытаемся получить все вызываемые CtConstructor
            CtExecutable<?> execDeclaration = executableRef.getExecutableDeclaration();

            if (execDeclaration instanceof CtConstructor<?>) {
                CtConstructor<?> calleeConstructor = (CtConstructor<?>) execDeclaration;
                calleeConstructors.add(
                        new Edge(
                                node,
                                calleeConstructor,
                                RelationType.CALLS,
                                constructorCall,
                                Label.KNOWN,
                                Condition.ALWAYS
                        )
                );
            } else if (execDeclaration == null) {
                // Метод не разрешен — используем информацию из референса

                calleeConstructors.add(
                        new Edge(
                                node,
                                null,
                                RelationType.CALLS,
                                constructorCall,
                                Label.UNKNOWN,
                                Condition.ALWAYS
                        )
                );
            }
        }
        return calleeConstructors;
    }

//    public List<FollowedDatum> isArgOfCalleeMethod(String datumName, CtInvocation<?> ctInvocation) {
//        int argInd = 0;
//        CtExecutableReference<?> executableRef = ctInvocation.getExecutable();
//        List<FollowedDatum> newData = new ArrayList<>();
//        for (CtExpression<?> arg : ctInvocation.getArguments()) {
//            // Если это переменная - получить её имя
//            if (arg instanceof CtVariableRead) {
//                String argName = ((CtVariableRead<?>) arg).getVariable().getSimpleName();
//                System.out.println("нашли invocation с переменной " + argName);
//                if (argName.equals(datumName)) {
//                    newData.add(new MethodArgument(
//                            new MethodLocation(
//                                    executableRef.getDeclaringType().getPackage().getQualifiedName(),
//                                    executableRef.getDeclaringType().getSimpleName(),
//                                    new Method(executableRef.getSimpleName(), executableRef.getParameters().size()),
//                                    argInd
//                            )
//                    ));
//                }
//            }
//            ++argInd;
//        }
//        return newData;
//    }

//    private String getFullMethodSignature(CtMethod<?> method) {
//        String params = method.getParameters().stream()
//                .map(p -> p.getType().getQualifiedName())
//                .collect(Collectors.joining(","));
//        return method.getDeclaringType().getQualifiedName() + "." +
//                method.getSimpleName() + "(" + params + ")";
//    }

    public static boolean contains(Set<FollowedDatum> isVisited, FollowedDatum requiredDatum) {
        for (FollowedDatum datum : isVisited) {
            if (datum.isEquals(requiredDatum)) {
                return true;
            }
        }
        return false;
    }

    public static String getFullSignatureForMethod(CtMethod<?> method) {
        return method.getDeclaringType().getQualifiedName() + "#" + method.getSignature();
    }

    public static String getFullSignatureForConstructor(CtConstructor<?> method) {
        return method.getDeclaringType().getQualifiedName() + "#" + method.getSignature();
    }

    public static boolean isStructuralBlock(CtElement element) {
        return element instanceof CtBlock ||
                element instanceof CtIf ||
                element instanceof CtLoop ||
                element instanceof CtTry ||
                element instanceof CtSwitch ||
                element instanceof CtSynchronized;
    }

    public List<CtStatement> getInnerStatementsFromBlock(CtElement blockElement) {
        if (!isStructuralBlock(blockElement)) {
            return null;
        }
        if (blockElement instanceof CtBlock) {
            return ((CtBlock<?>) blockElement).getStatements();
        }

        if (blockElement instanceof CtIf) {
            CtIf ifStmt = (CtIf) blockElement;
            List<CtStatement> result = new ArrayList<>();

            // Then-ветка
            CtStatement thenPart = ifStmt.getThenStatement();
            result.addAll(getStatementsFromPart(thenPart));

            // Else-ветка
            CtStatement elsePart = ifStmt.getElseStatement();
            if (elsePart != null) {
                result.addAll(getStatementsFromPart(elsePart));
            }

            return result;
        }

        if (blockElement instanceof CtLoop) {
            CtLoop loop = (CtLoop) blockElement;
            return getStatementsFromPart(loop.getBody());
        }

        if (blockElement instanceof CtTry) {
            CtTry tryStmt = (CtTry) blockElement;
            List<CtStatement> result = new ArrayList<>();

            // Try-блок
            result.addAll(tryStmt.getBody().getStatements());

            // Catch-блоки
            for (CtCatch catcher : tryStmt.getCatchers()) {
                result.addAll(catcher.getBody().getStatements());
            }

            // Finally-блок
            CtBlock<?> finallyBlock = tryStmt.getFinalizer();
            if (finallyBlock != null) {
                result.addAll(finallyBlock.getStatements());
            }

            return result;
        }

        if (blockElement instanceof CtSynchronized) {
            return ((CtSynchronized) blockElement).getBlock().getStatements();
        }

        if (blockElement instanceof CtCase) {
            return ((CtCase<?>) blockElement).getStatements();
        }

        return new ArrayList<>();
    }

    private List<CtStatement> getStatementsFromPart(CtStatement part) {
        if (part instanceof CtBlock) {
            return ((CtBlock<?>) part).getStatements();
        } else if (part != null) {
            return Arrays.asList(part);
        }
        return new ArrayList<>();
    }

//    public MethodArgument getMethodArgumentFromDescription(CtModel ctModel, String fullSignature, int positionInMethod) {
//        // 2. Поиск метода по сигнатуре во всей модели
//        CtMethod<?> targetMethod = null;
//        for (CtClass<?> clazz : ctModel.getElements(new TypeFilter<>(CtClass.class))) {
//            for (CtMethod<?> method : clazz.getMethods()) {
//                String curFullSignature = getFullSignatureForMethod(method);
//                if (fullSignature.equals(curFullSignature)) {
//                    targetMethod = method;
//                    break;
//                }
//            }
//            if (targetMethod != null) break;
//        }
//
//        // 3. Проверка, что метод найден
//        if (targetMethod == null) {
//            throw new RuntimeException("Метод с сигнатурой '" + fullSignature + "' не найден в модели");
//        }
//
//        // 4. Получение параметра по позиции
//        var parameters = targetMethod.getParameters();
//        if (positionInMethod < 0 || positionInMethod >= parameters.size()) {
//            throw new RuntimeException(
//                    String.format("Неверная позиция параметра: %d. Метод '%s' имеет %d параметров",
//                            positionInMethod, fullSignature, parameters.size())
//            );
//        }
//        return new MethodArgument(parameters.get(positionInMethod));
//    }

}

package org.example.structure.auxiliary_functions;

import org.example.analizer.Method;
import org.example.analizer.followed_data.FollowedDatum;
import org.example.analizer.followed_data.location.MethodArgumentLocation;
import org.example.structure.StartMethod;
import org.example.structure.graph.Condition;
import org.example.structure.graph.Edge;
import org.example.structure.graph.Label;
import org.example.structure.graph.RelationType;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.List;

public class Functions {
    public boolean checkIsMethodMatchesStructure(CtMethod<?> ctMethod, StartMethod startMethod) {
        return ctMethod.getSimpleName().equals(startMethod.getMethodName()) &&
                ctMethod.getParameters().size() == startMethod.getMethodArguments().size() &&
                ctMethod.getDeclaringType().getSimpleName().equals(startMethod.getClassName());
    }

    public boolean checkIsConstructorMatchesStructure(CtConstructor<?> ctConstructor, StartMethod startMethod) {
        return ctConstructor.getDeclaringType().getSimpleName().equals(startMethod.getMethodName()) &&
                ctConstructor.getParameters().size() == startMethod.getMethodArguments().size() &&
                ctConstructor.getDeclaringType().getSimpleName().equals(startMethod.getClassName());
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

    public List<FollowedDatum> isArgOfCalleeMethod(String datumName, CtInvocation<?> ctInvocation) {
        int argInd = 0;
        CtExecutableReference<?> executableRef = ctInvocation.getExecutable();
        List<FollowedDatum> newData = new ArrayList<>();
        for (CtExpression<?> arg : ctInvocation.getArguments()) {
            // Если это переменная - получить её имя
            if (arg instanceof CtVariableRead) {
                String argName = ((CtVariableRead<?>) arg).getVariable().getSimpleName();
                System.out.println("нашли invocation с переменной " + argName);
                if (argName.equals(datumName)) {
                    newData.add(new FollowedDatum(
                            new MethodArgumentLocation(
                                    executableRef.getDeclaringType().getPackage().getQualifiedName(),
                                    executableRef.getDeclaringType().getSimpleName(),
                                    new Method(executableRef.getSimpleName(), executableRef.getParameters().size()),
                                    argInd
                            )
                    ));
                }
            }
            ++argInd;
        }
        return newData;
    }
}

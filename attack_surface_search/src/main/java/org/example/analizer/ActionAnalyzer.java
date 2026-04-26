package org.example.analizer;

import org.example.analizer.followed_data.EquatingLocation;
import org.example.analizer.followed_data.FollowedData;
import org.example.analizer.followed_data.FollowedDataLocation;
import org.example.analizer.followed_data.MethodArgumentLocation;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;

import java.util.List;
import java.util.Objects;

public class ActionAnalyzer {
    // 1. Вызовы и создание объектов
    // data передается куда-то как аргумент:

    // CtInvocation — вызов обычного метода (не конструктора).
    public static FollowedData analyzeActionCtInvocation(CtInvocation<?> call, CtVariableRead<?> access) {
        FollowedData newData = new FollowedData();
        // список всех аргументов
        List<CtExpression<?>> args = call.getArguments();
        // количество аргументов
        int argsCount = args.size();
        // индекс data в этом списке
        int argumentIndex = args.indexOf(access);
        // имя метода
        String methodName = call.getExecutable().getSimpleName();
        // имя класса, которому принадлежит метод
        String className = "Unknown";
        String packageName = "Unknown";

        // 1. Получаем "цель" вызова (то, что стоит до точки)
        CtExpression<?> target = call.getTarget();

        if (target != null) {
            System.out.println("Данные переданы в метод объекта: " + target.toString());

            // 2. Если цель — это обращение к переменной (anotherData.method(data))
            if (target instanceof CtVariableAccess) {
                System.out.println("цель — это обращение к переменной");

                // 1. Получаем ссылку на переменную
                CtVariableReference<?> reference = access.getVariable();
                // 2. Получаем само объявление (место, где она создана)
                CtVariable<?> declaration = reference.getDeclaration();

                if (declaration == null) {
                    // поле сторонней либы: System.out
                    // поля возникшие из-за аннотаций
                    // сложные конструкции, не явные присваивания (ламбок и тд)
                    System.out.printf("неизвестность:: нельзя найти источник");
                } 
                // локальная переменная
                else if (declaration instanceof CtLocalVariable) {
                    // конструктор - ок
                    // инициализирована через другой метод - найти метод, определить по коду возвращаемое значение, если интерфейс
                    // присваивание - найти ту переменную, понять её тип = рекурсия
                    // Получаем выражение, которым инициализирована переменная
                    CtExpression<?> initializer = declaration.getDefaultExpression();

                    if (initializer instanceof CtConstructorCall) {
                        System.out.println("Этой переменной присвоен результат вызова конструктора!");

                        CtConstructorCall<?> consCall = (CtConstructorCall<?>) initializer;
                        // Можно узнать, какой именно класс создается
                        System.out.println("Создается объект типа: " + consCall.getType().getQualifiedName());
                    } else {
                        CtVariableAccess<?> targetAccess = (CtVariableAccess<?>) target;
                        String anotherDataName = targetAccess.getVariable().getSimpleName();
                        String anotherDataType = targetAccess.getVariable().getType().getQualifiedName();

                        System.out.println("Найдена переменная-приемник: " + anotherDataName + " [тип: " + anotherDataType + "]");

                        // Здесь вы можете создать новый объект FollowedData
                        // и пометить, что теперь мы "следим" за anotherData в этом методе
                        newData = new FollowedData();
//                newData.setName(anotherDataName);
                        // newData.setClassName(anotherDataType); // и т.д.
                        return newData;
                    }
                }
                // параметр анализируемого метода
                else if (declaration instanceof CtParameter) {
                    // если класс - ок
                    // если интерфейс:
                    // по цепочке назад посмотреть что передано в этот вызов метода
                    // повторить до тех пор, пока не найден, рекурсия
                    // может ли быть никогда не найден?
                    CtVariableAccess<?> targetAccess = (CtVariableAccess<?>) target;
                    String anotherDataName = targetAccess.getVariable().getSimpleName();
                    String anotherDataType = targetAccess.getVariable().getType().getQualifiedName();

                    System.out.println("Найдена переменная-приемник: " + anotherDataName + " [тип: " + anotherDataType + "]");

                    // Здесь вы можете создать новый объект FollowedData
                    // и пометить, что теперь мы "следим" за anotherData в этом методе
                    newData = new FollowedData();
//                newData.setName(anotherDataName);
                    // newData.setClassName(anotherDataType); // и т.д.
                    return newData;
                }
                // поле анализируемого класса
                else if (declaration instanceof CtField) {
                    // если класс - ок
                    // если интерфейс:
                    // по цепочке назад посмотреть момент инициализации поля
                    // повторить до тех пор, пока не найден, рекурсия
                    // может ли быть никогда не найден?
                    CtVariableAccess<?> targetAccess = (CtVariableAccess<?>) target;
                    String anotherDataName = targetAccess.getVariable().getSimpleName();
                    String anotherDataType = targetAccess.getVariable().getType().getQualifiedName();

                    System.out.println("Найдена переменная-приемник: " + anotherDataName + " [тип: " + anotherDataType + "]");

                    // Здесь вы можете создать новый объект FollowedData
                    // и пометить, что теперь мы "следим" за anotherData в этом методе
                    newData = new FollowedData();
//                newData.setName(anotherDataName);
                    // newData.setClassName(anotherDataType); // и т.д.
                    return newData;
                } else {
                    // CtCatchVariable
                    // CtEnumConstant
                    System.out.printf("неизвестность:: %s%n", declaration.getDefaultExpression());
                }
            }
            // 3. Если метод вызван у результата другого метода (getObj().method(data))
            else if (target instanceof CtInvocation) {
                System.out.println("Цель вызова — результат другого метода: " + target);
            } else {
                // например String.toString()
                System.out.printf("неизвестность:: %s%n", target.getShortRepresentation());
            }
        } else {
            // Если target == null, значит метод вызван у текущего класса (this или статический метод)
            System.out.println("Метод вызван в текущем классе (this или static)");

            CtTypeReference<?> declaringType = call.getExecutable().getDeclaringType();

            // declaringType может быть null, если это класс сторонней библиотеки, к которой нет доступа
            if (call.getExecutable().getDeclaringType() != null) {
                className = call.getExecutable().getDeclaringType().getSimpleName();
            }
            // Если null, смотрим на объект, у которого вызван метод - чтобы понять название библиотеки
            else if (call.getTarget() != null && call.getTarget().getType() != null) {
                className = call.getTarget().getType().getQualifiedName();
            } else {
                System.out.println("Не могу распознать класс");
            }

            if (!Objects.equals(className, "Unknown")) {
                // 3. Получаем ссылку на пакет
                var pack = declaringType.getPackage();

                packageName = (pack != null) ? pack.getQualifiedName() : "Unknown";
            } else {
                // Если Spoon не смог разрешить зависимости (No-Classpath mode)
                System.out.println("Информация о пакете недоступна (не удалось разрешить тип)");
            }
            Method method = new Method(methodName, argsCount);
            MethodArgumentLocation location = new MethodArgumentLocation(
                    packageName, className, method, argumentIndex
            );
            newData.setLocation(location);

            System.out.println("Вызов: Параметр передан в метод [" + methodName + "] класса [" + className + "] пакета [" + packageName + "]");
        }
        return newData;
    }

    // CtConstructorCall — вызов конструктора (new MyClass(data)).
    public static FollowedData analyzeActionCtConstructorCall(CtConstructorCall<?> call, CtVariableRead<?> access) {
        FollowedData newData = new FollowedData();
        // список всех аргументов
        List<CtExpression<?>> args = call.getArguments();
        // количество аргументов
        int argsCount = args.size();
        // индекс data в этом списке
        int argumentIndex = args.indexOf(access);
        // имя метода
        String methodName = call.getExecutable().getSimpleName();
        // имя класса, которому принадлежит метод
        String className = "Unknown";
        String packageName = "Unknown";
        CtTypeReference<?> declaringType = call.getExecutable().getDeclaringType();

        // declaringType может быть null, если это класс сторонней библиотеки, к которой нет доступа
        if (call.getExecutable().getDeclaringType() != null) {
            className = call.getExecutable().getDeclaringType().getQualifiedName();
        }
        // Если null, смотрим на объект, у которого вызван метод - чтобы понять название библиотеки
        else if (call.getTarget() != null && call.getTarget().getType() != null) {
            className = call.getTarget().getType().getQualifiedName();
        } else {
            System.out.println("Не могу распознать класс");
        }

        if (!Objects.equals(className, "Unknown")) {
            // 3. Получаем ссылку на пакет
            var pack = declaringType.getPackage();

            packageName = (pack != null) ? pack.getQualifiedName() : "Unknown";
        } else {
            // Если Spoon не смог разрешить зависимости (No-Classpath mode)
            System.out.println("Информация о пакете недоступна (не удалось разрешить тип)");
        }
        Method method = new Method(methodName, argsCount);
        MethodArgumentLocation location = new MethodArgumentLocation(
                packageName, className, method, argumentIndex
        );
        newData.setLocation(location);

        System.out.println("Вызов: Параметр передан в конструктор [" + methodName + "] класса [" + className + "] пакета [" + packageName + "]");
        return newData;
    }
    // CtExecutableReference — ссылка на метод.
    // TODO

    // 2. Операции и выражения
    // с data производятся вычисления:
    // CtBinaryOperator — бинарная операция (data + 1, data == null, data && b).
    // TODO
    // CtUnaryOperator — унарная операция (!data, data++).
    // TODO

    // CtAssignment — присваивание (data = 5).
    public static FollowedData analyzeActionCtAssignment(CtAssignment<?,?> assignment, CtVariableRead<?> access) {
        FollowedData newData = new FollowedData();
        // куда присваиваем
        CtExpression<?> leftHand = assignment.getAssigned();

        // имя переменной, в которую присвоили
        String dataName = "unknown";

        if (leftHand instanceof CtVariableAccess<?> varAccess) {
            // Если это просто переменная (anotherData = data)
            dataName = varAccess.getVariable().getSimpleName();
            System.out.println("Значение присвоено в: " + dataName);
        } else {
            // Если это что-то сложное (например, массив: array[0] = data)
            dataName = leftHand.toString();
            System.out.println("Присваивание в сложную конструкцию: " + dataName);
        }
        // Поднимаемся выше по дереву, чтобы найти контекст

        // Ищем метод
        CtMethod<?> parentMethod = assignment.getParent(CtMethod.class);
        String methodName = (parentMethod != null) ? parentMethod.getSimpleName() : "Unknown";
        int argsCount = -1;
        if (parentMethod != null) {
            argsCount = parentMethod.getParameters().size();
        }

        // Ищем класс
        CtType<?> parentClass = assignment.getParent(CtType.class);
        String className = (parentClass != null) ? parentClass.getQualifiedName() : "Unknown";

        // Ищем пакет
        CtPackage parentPackage = assignment.getParent(CtPackage.class);
        String packageName = (parentPackage != null) ? parentPackage.getQualifiedName() : "Unknown";


        Method method = new Method(methodName, argsCount);
        EquatingLocation location = new EquatingLocation(
                packageName, className, method
        );
        newData.setLocation(location);

        System.out.println("Вызов: Параметр передан в метод [" + methodName + "] класса [" + className + "] пакета [" + packageName + "]");
        return newData;
    }

    // CtConditional — тернарный оператор (data ? x : y).
    // TODO

    // 3. Управляющие конструкции (Statements)
    // data управляет логикой:
    // CtIf — условие (if (data) { ... }).
    // TODO
    // CtWhile, CtFor, CtDo — циклы.
    // TODO
    // CtSwitch — выбор значения.
    // TODO
    // CtReturn — возврат из метода (return data;).
    // TODO
    // CtThrow — выброс исключения (throw data;).
    // TODO
    // 4. Работа со структурами данных
    // **CtArrayRead / CtArrayWrite — доступ к массиву (data[0]).
    // TODO
    // CtFieldAccess — доступ к полю объекта (data.id).
    // TODO
    // CtAssert — проверка assert data != null.
    // TODO
    // 5. Технические узлы
    // CtBlock — фигурные скобки { ... }, содержит список инструкций.
    // TODO
    // CtLocalVariable — объявление переменной (int x = data;).
    // TODO
}

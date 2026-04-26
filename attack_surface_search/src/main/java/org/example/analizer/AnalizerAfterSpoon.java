package org.example.analizer;

import org.example.analizer.followed_data.FollowedData;
import org.example.analizer.followed_data.FollowedDataLocation;
import org.example.analizer.followed_data.MethodArgumentLocation;
import org.example.analizer.result_structure.ResultStructureNode;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;
import java.util.stream.Collectors;

public class AnalizerAfterSpoon {
    private CtModel model;
    private FollowedData startFollowedData;
    public AnalizerAfterSpoon(String inputDataJson) throws Exception {
        this.startFollowedData = JsonToClassGenerator.createDataFromLocationJson(inputDataJson);
    }
    public void setModel(CtModel model) {
        this.model = model;
    }
    public ResultStructureNode analyze() {
        ResultStructureNode resultStructureNode = new ResultStructureNode();
        Queue<FollowedData> queueForNextClass = new ArrayDeque<>();
        queueForNextClass.add(startFollowedData);
        ResultStructureNode curResult = resultStructureNode;

//        model.getAllTypes().forEach(type -> {
//            String kind = "";
//
//            // Определяем, что это за тип
//            if (type.isInterface()) kind = "[Interface]";
//            else if (type.isEnum()) kind = "[Enum]";
//            else if (type.isClass()) kind = "[Class]";
//            else kind = "[Other]";
//
//            // Печатаем полное имя (с пакетом) или просто имя
//            System.out.println(kind + " " + type.getQualifiedName());
//        });

        while (!queueForNextClass.isEmpty()) {
            // извлекает и удаляет первый в очереди
            FollowedData curData = queueForNextClass.poll();
            // TODO сделать поведение не как список, а как дерево

            curResult.addChild(new ResultStructureNode(
                    curData.getLocation()
            ));
            curResult = curResult.getChildren().get(0);
            ArrayList<FollowedData> newDatas = analiseClass(curData);
            if (!newDatas.isEmpty()) {
                queueForNextClass.addAll(newDatas);
            }
        }
        resultStructureNode = resultStructureNode.getChildren().get(0);
        return resultStructureNode;
    }

    private ArrayList<FollowedData> analiseClass(FollowedData data) {
        ArrayList<FollowedData> newDatas = new ArrayList<>();
        FollowedDataLocation rowLocation = data.getLocation();
        if (rowLocation instanceof MethodArgumentLocation) {
            MethodArgumentLocation location = (MethodArgumentLocation) rowLocation;
            CtMethod<?> method = getRequiredMethod(location.getJavaPackage(), location.getJavaClass(), location.getMethod());

            if (method != null) {
                int index = location.getPositionInMethod();
                List<CtParameter<?>> params = method.getParameters();

                if (index >= 0 && index < params.size()) {
                    CtParameter<?> targetParam = params.get(index);

                    // Ищем все упоминания (ссылки) на этот параметр внутри тела метода
                    List<CtVariableAccess<?>> accessPoints = method
                            .getElements(new TypeFilter<CtVariableAccess<?>>(CtVariableAccess.class))
                            .stream()
                            .filter(access -> access.getVariable().getDeclaration() == targetParam)
                            .toList();

                    for (CtVariableAccess<?> access : accessPoints) {
                        FollowedData newData = analyzeAction(access);
                        if (newData != null) {
                            newDatas.add(newData);
                        }
                    }
                } else {
                    System.out.println("Неверная FollowedData");
                }
            }
        }

//        System.out.println(name);
        return newDatas;
    }

    private CtMethod<?> getRequiredMethod(String javaPackageName, String javaClassName, Method methodStructure) {
        List<CtPackage> packages = model.getElements(new TypeFilter<>(CtPackage.class)).stream()
                .filter(curPackage -> curPackage != null && javaPackageName.equals(curPackage.getQualifiedName()))
                .toList();
        if (packages.isEmpty()) {
            System.out.println("Не найден класс");
            return null;
        } else if (packages.size() > 1) {
            System.out.println("Класс не единственный - некорректное описание");
            return null;
        }
        CtPackage javaPackage = packages.get(0);
        List<CtClass<?>> classes = javaPackage.getTypes().stream()
                .filter(type -> type instanceof CtClass) // Оставляем только классы (enum и интерфейсы отсеются)
                .map(type -> (CtClass<?>) type)          // Приводим к CtClass
                .filter(clazz -> javaClassName.equals(clazz.getSimpleName()))
                .collect(Collectors.toList());
        if (classes.isEmpty()) {
            System.out.println("Не найден класс");
            return null;
        } else if (classes.size() > 1) {
            System.out.println("Класс не единственный - некорректное описание");
            return null;
        }
        CtClass<?> javaClass = classes.get(0);
        List<CtMethod<?>> method = javaClass.getMethods().stream() // Используем getMethods()
                .filter(m -> methodStructure.getName().equals(m.getSimpleName())) // Проверка имени
                .filter(m -> m.getParameters().size() == methodStructure.getNumberOfArguments()) // Проверка кол-ва аргументов
                .toList();
        if (method.isEmpty()) {
            System.out.println("Не найден метод");
            return null;
        } else if (method.size() > 1) {
            System.out.println("Метод не единственный - некорректное описание");
            return null;
        }
        System.out.println("Найден метод: " + method.get(0).getSignature());
        return method.get(0);
    }
    public FollowedData analyzeAction(CtVariableAccess<?> access) {
        CtElement oneWhoOperatesOnData = access.getParent();
        FollowedData newData = new FollowedData();

        if (access instanceof CtVariableWrite) {
            System.out.println("Изменение: Параметру присваивается новое значение: " + oneWhoOperatesOnData);
        } else if (access instanceof CtVariableRead) {
            // вызов обычного метода, не конструктора
            if (oneWhoOperatesOnData instanceof CtInvocation) {
                // Проверяем роль data в этом вызове:
                // она вызывает метод, или её вызывают в методе ?
                if (access.getRoleInParent() == CtRole.ARGUMENT) {
                    // СЮДА попадут: process(data), Math.max(data, 10) и т.д.
                    System.out.println("Вызов: Параметр передан в метод " + ((CtInvocation<?>)oneWhoOperatesOnData).getExecutable().getSimpleName());
                    return ActionAnalyzer.analyzeActionCtInvocation((CtInvocation<?>) oneWhoOperatesOnData, (CtVariableRead<?>) access);
                }
                else if (access.getRoleInParent() == CtRole.TARGET) {
                    // СЮДА попадут: data.method()
                    System.out.println("Вызов: Параметр вызывает метод " + ((CtInvocation<?>)oneWhoOperatesOnData).getExecutable().getSimpleName());
                    return null;
                }
            }
            // вызов конструктора
            else if (oneWhoOperatesOnData instanceof CtConstructorCall) {
                System.out.println("Вызов: Параметр передан в конструктор " + ((CtConstructorCall<?>)oneWhoOperatesOnData).getExecutable().getSimpleName());
                return ActionAnalyzer.analyzeActionCtConstructorCall((CtConstructorCall<?>) oneWhoOperatesOnData, (CtVariableRead<?>) access);
            }
            // присваивание в существующую переменную
            else if (oneWhoOperatesOnData instanceof CtAssignment) {
                System.out.println("Операция: Параметр участвует в присваивании в ранее созданную переменную: " + ((CtAssignment)oneWhoOperatesOnData).getShortRepresentation());
                return ActionAnalyzer.analyzeActionCtAssignment((CtAssignment<?, ?>) oneWhoOperatesOnData, (CtVariableRead<?>) access);
            }
//            // присваивание в создающуюся переменную
//            else if (oneWhoOperatesOnData instanceof CtLocalVariable) {
//                System.out.println("Операция: Параметр участвует в присваивании в создающуюся (локальную) переменную: " + oneWhoOperatesOnData.getShortRepresentation());
//                return analizeActionCtLocalVariable((CtLocalVariable<?>) oneWhoOperatesOnData, (CtVariableRead<?>) access);
//            }
//            // return data
//            else if (oneWhoOperatesOnData instanceof CtReturn) {
//                System.out.println("Возврат: Параметр возвращается из метода");
//                return analizeActionCtReturn((CtReturn<?>) oneWhoOperatesOnData, (CtVariableRead<?>) access);
//            }
            // остальное
            else {
                System.out.println("Чтение: Параметр используется в: " + oneWhoOperatesOnData.getClass().getSimpleName());
            }
        } else {
            System.out.println("Неизвестный тип операции над параметром");
        }
        return null;
    }

}

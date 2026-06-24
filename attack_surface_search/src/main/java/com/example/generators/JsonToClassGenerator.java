package com.example.generators;

import com.example.entry_points.EntryPoint;
import com.example.entry_points.InputEntryPoint;
import com.example.input_structure.InputStructureLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.example.analizer.followed_data.FollowedDatum;
//import com.example.analizer.followed_data.location.MethodLocation;
//import com.example.analizer.project_structure.ProjectStructureGraph;
//import com.example.analizer.project_structure.ProjectStructureNode;
import com.example.input_structure.InputStructureMethod;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonToClassGenerator {
//    String inputStructureJson = "project_structure/structure.json"; // Путь к вашему JSON-файлу

//    String inputDatumJson = "project_structure/data.json";
//    public static ProjectStructureGraph createStructureGraphFromJson(String inputStructureJson) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        ProjectStructureNode node = new ProjectStructureNode();
//
//        try {
//            File jsonFile = new File(inputStructureJson);
//
//            // Читаем JSON и создаем объект
//            mapper.readerForUpdating(node).readValue(jsonFile);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ProjectStructureGraph(node);
//    }

//    public static FollowedDatum createDatumFromLocationJson(String inputDatumJson) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        MethodLocation location = new MethodLocation();
//
//        try {
//            File jsonFile = new File(inputDatumJson);
//
//            // Читаем JSON и создаем объект
//            mapper.readerForUpdating(location).readValue(jsonFile);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        FollowedDatum newDatum = new MethodArgument();
//        newDatum.setLocation(location);
//        return newDatum;
//    }

    public static InputStructureMethod createMethodStructureFromJson(String inputDatumJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStructureMethod method = new InputStructureMethod();

        try {
            File jsonFile = new File(inputDatumJson);

            // Читаем JSON и создаем объект
            mapper.readerForUpdating(method).readValue(jsonFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return method;
    }

    public static InputStructureLocation createInputStructureFromJson(String inputDatumJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStructureLocation location = new InputStructureLocation();

        try {
            File jsonFile = new File(inputDatumJson);

            // Читаем JSON и создаем объект
            mapper.readerForUpdating(location).readValue(jsonFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public static InputEntryPoint[] createEntryPointsFromJson(String inputArrJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputEntryPoint[] entryPointsInput = new InputEntryPoint[0];
        try {
            File jsonFile = new File(inputArrJson);

            // Читаем JSON и создаем объект
            entryPointsInput = mapper.readValue(jsonFile, InputEntryPoint[].class);
//            System.out.println(entryPointsInput.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entryPointsInput;
    }

//    public static CtParameter<?> createInputStructureFromJson(String inputMethodJson, CtModel ctModel) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        InputStructureLocation location = new InputStructureLocation();
//
//        try {
//            File jsonFile = new File(inputDatumJson);
//
//            // Читаем JSON и создаем объект
//            mapper.readerForUpdating(location).readValue(jsonFile);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return location;
//    }


    public static CtParameter<?> createArrEntryPointsFromJson(String inputMethodJsonFile, CtModel ctModel) {
        try {
            File jsonFile = new File(inputMethodJsonFile);
            // 1. Читаем JSON файл
            ObjectMapper objectMapper = new ObjectMapper();
            var jsonNode = objectMapper.readTree(jsonFile);

            String signature = jsonNode.get("signature").asText();
            int positionInMethod = jsonNode.get("positionInMethod").asInt();

            // 2. Поиск метода по сигнатуре во всей модели
            CtMethod<?> targetMethod = null;
            for (CtType<?> type : ctModel.getAllTypes()) {
                for (CtMethod<?> method : type.getMethods()) {
                    String fullSignature = method.getDeclaringType().getQualifiedName() + "#" + method.getSignature();
                    if (fullSignature.equals(signature)) {
                        targetMethod = method;
                        break;
                    }
                }
                if (targetMethod != null) break;
            }

            // 3. Проверка, что метод найден
            if (targetMethod == null) {
                throw new RuntimeException("Метод с сигнатурой '" + signature + "' не найден в модели");
            }

            // 4. Получение параметра по позиции
            var parameters = targetMethod.getParameters();
            if (positionInMethod < 0 || positionInMethod >= parameters.size()) {
                throw new RuntimeException(
                        String.format("Неверная позиция параметра: %d. Метод '%s' имеет %d параметров",
                                positionInMethod, signature, parameters.size())
                );
            }

            return parameters.get(positionInMethod);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении JSON файла с сигнатурой метода: ", e);
        }
    }
}


package org.example.analizer;
import org.example.analizer.structures.FollowedData;
import org.example.analizer.structures.ProjectStructureGraph;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;
public class ProjectAnalizer {
//    private String inputDataJson;
    private Path projectPath;
    private FollowedData startFollowedData;
    private ClassAnaliser classAnaliser;
//    private JsonToClassGenerator jsonToClassGenerator;
//    private ClassAnaliser classAnaliser;

    public ProjectAnalizer(String inputDataJson, String projectStructure, Path projectPath) throws Exception {
//        this.inputDataJson = inputDataJson;
        this.projectPath = projectPath;
        this.startFollowedData = JsonToClassGenerator.createDataFromJson(inputDataJson);
        this.classAnaliser = new ClassAnaliser(JsonToClassGenerator.createStructureGraphFromJson(projectStructure));
    }
    public void analiseProject() throws IOException {
        Queue<FollowedData> queueForNextClass = new ArrayDeque<>();
        queueForNextClass.add(startFollowedData);

        while (!queueForNextClass.isEmpty()) {
            // извлекает и удаляет первый в очереди
            FollowedData curData = queueForNextClass.poll();
            // читает всё содержимое нужного файла по строчкам
            List<String> content = Files.readAllLines(Path.of(curData.getFile()));
            // анализирует данные нужного класса и возвращает следующие точки для анализа
            queueForNextClass.addAll(classAnaliser.analiseClass(content , curData));
        }

//        try (Stream<Path> paths = Files.walk(projectPath)) {
//            paths
//                    // Фильтруем: только файлы, заканчивающиеся на .java
//                    .filter(Files::isRegularFile)
//                    .filter(path -> path.toString().endsWith(".java"))
//                    .forEach(path -> {
//                        System.out.println("--- Читаем файл: " + path.getFileName() + " ---");
//                        try {
//                            // Читаем всё содержимое файла
//                            List<String> content = Files.readAllLines(path);
//                            int size = content.size();
//                            ClassAnaliser classAnaliser = new ClassAnaliser(size);
//                            System.out.println("file lines count = " + size+ "\n");
//
//                            classAnaliser.analiseClass(content , data);
//                            classAnaliser.print(data);
//                        } catch (IOException e) {
//                            System.err.println("Ошибка при чтении файла " + path + ": " + e.getMessage());
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

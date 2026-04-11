package org.example.analizer;
import org.example.analizer.followed_data.FollowedData;
import org.example.analizer.result_structure.ResultStructureNode;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayDeque;
import java.util.Queue;

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
//        System.out.println(startFollowedData.getName());
//        startFollowedData.print();
        this.classAnaliser = new ClassAnaliser(JsonToClassGenerator.createStructureGraphFromJson(projectStructure));

    }
    public ResultStructureNode analiseProject() throws IOException {
        ResultStructureNode resultStructureNode = new ResultStructureNode("parent structure", 0);
        Queue<FollowedData> queueForNextClass = new ArrayDeque<>();
        queueForNextClass.add(startFollowedData);
        ResultStructureNode curResult = resultStructureNode;
//        FollowedData prevData = startFollowedData;

//        System

        while (!queueForNextClass.isEmpty()) {
            // извлекает и удаляет первый в очереди
            FollowedData curData = queueForNextClass.poll();
            // TODO сделать поведение не как список, а как дерево

            curResult.addChild(new ResultStructureNode(curData.getMethod().getName(), curData.getPositionInMethod()));
            curResult = curResult.getChildren().get(0);
            // читает всё содержимое нужного файла по строчкам
//            List<String> content = Files.readAllLines(Path.of(curData.getFile()));
            // анализирует данные нужного класса и возвращает следующие точки для анализа
            queueForNextClass.addAll(classAnaliser.analiseClass(curData));
        }

        return resultStructureNode;
    }
}

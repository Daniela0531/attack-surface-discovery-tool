package org.example.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.analizer.followed_data.FollowedDatum;
import org.example.analizer.followed_data.MethodArgumentLocation;
import org.example.analizer.project_structure.ProjectStructureGraph;
import org.example.analizer.project_structure.ProjectStructureNode;
import org.example.structure.StartMethod;

import java.io.File;

public class JsonToClassGenerator {
//    String inputStructureJson = "project_structure/structure.json"; // Путь к вашему JSON-файлу

//    String inputDatumJson = "project_structure/data.json";
    public static ProjectStructureGraph createStructureGraphFromJson(String inputStructureJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ProjectStructureNode node = new ProjectStructureNode();

        try {
            File jsonFile = new File(inputStructureJson);

            // Читаем JSON и создаем объект
            mapper.readerForUpdating(node).readValue(jsonFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProjectStructureGraph(node);
    }

    public static FollowedDatum createDatumFromLocationJson(String inputDatumJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MethodArgumentLocation location = new MethodArgumentLocation();

        try {
            File jsonFile = new File(inputDatumJson);

            // Читаем JSON и создаем объект
            mapper.readerForUpdating(location).readValue(jsonFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        FollowedDatum newDatum = new FollowedDatum();
        newDatum.setLocation(location);
        return newDatum;
    }

    public static StartMethod createMethodStructureFromJson(String inputDatumJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StartMethod method = new StartMethod();

        try {
            File jsonFile = new File(inputDatumJson);

            // Читаем JSON и создаем объект
            mapper.readerForUpdating(method).readValue(jsonFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return method;
    }
}

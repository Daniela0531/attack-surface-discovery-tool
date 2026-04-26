package org.example.analizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.analizer.followed_data.FollowedData;
import org.example.analizer.followed_data.MethodArgumentLocation;
import org.example.analizer.project_structure.ProjectStructureGraph;
import org.example.analizer.project_structure.ProjectStructureNode;

import java.io.File;

public class JsonToClassGenerator {
//    String inputStructureJson = "project_structure/structure.json"; // Путь к вашему JSON-файлу

//    String inputDataJson = "project_structure/data.json";
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

    public static FollowedData createDataFromLocationJson(String inputDataJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MethodArgumentLocation location = new MethodArgumentLocation();

        try {
            File jsonFile = new File(inputDataJson);

            // Читаем JSON и создаем объект
            mapper.readerForUpdating(location).readValue(jsonFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        FollowedData newData = new FollowedData();
        newData.setLocation(location);
        return newData;
    }
//        GenerationConfig config = new DefaultGenerationConfig() {
//            @Override
//            public boolean isGenerateBuilders() { return true; }
//            @Override
//            public SourceType getSourceType() { return SourceType.JSON; } // Указываем, что на входе JSON
//            @Override
//            public AnnotationStyle getAnnotationStyle() { return AnnotationStyle.JACKSON2; }
//        };
//
//        JCodeModel codeModel = new JCodeModel();
//        SchemaMapper mapper = new SchemaMapper(
//                new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()),
//                new SchemaGenerator()
//        );
//
//        // Генерация
//        mapper.generate(codeModel, "UserClassName", packageName, inputJson.toURI().toURL());
//
//        // Запись файлов на диск
//        if (!outputDirectory.exists()) outputDirectory.mkdirs();
//        codeModel.build(outputDirectory);
//
//        System.out.println("Готово! Классы созданы в: " + outputDirectory.getAbsolutePath());

}

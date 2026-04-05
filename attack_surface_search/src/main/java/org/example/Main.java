package org.example;
import org.example.analizer.ProjectAnalizer;
import org.example.analizer.structures.FollowedData;

import java.io.IOException;
import java.nio.file.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static Path dirForProjectCopy = Paths.get("preproccesed_project");
    public static void main(String[] args) throws Exception {
        String pathToProject = "/Users/daniela/Desktop/maga_diplom/test/";

        // копирование проекта в вспомогательную директорию

//        System.out.println("Клонирую проект ...");
//        ProjectCopyPreparation projectCopyPreparation = new ProjectCopyPreparation();
//        Path sourcePath = Paths.get(pathToProject);
//        try {
//            projectCopyPreparation.projectCopyPreparation(sourcePath , dirForProjectCopy);
//        } catch (IOException exception) {
//            System.err.println("Ошибка при обработке файла");
//            return;
//        }
//        System.out.println("Создание клона проекта завершено!");
//
//        // при успешном переносе исходника в директорию для работы
//        // выполнить препроцессинг кода
//        System.out.println("Препроцессирую текст кода проекта ...");
//        ProjectPreprocessor projectPreprocessor = new ProjectPreprocessor(dirForProjectCopy);
//        try {
//            projectPreprocessor.processProject();
//            System.out.println("Препроцессинг копии проекта завершен!");
//        } catch (IOException e) {
//            System.out.println("Ошибка препроцессинга копии проекта");
//            return;
//        }
//        System.out.println("Построение структуры проекта ...");
//        StructureBuilder structureBuilder = new StructureBuilder(dirForProjectCopy);
//        try {
//            structureBuilder.createProjectStructure();
//            System.out.println("Пострроение структуры завершено!");
//        } catch (IOException e) {
//            System.out.println("Ошибка пострроение структуры проекта");
//            return;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        String inputDataJson = "project_structure/data.json";
        String inputStructureJson = "project_structure/structure.json";
        ProjectAnalizer projectAnalizer = new ProjectAnalizer(inputDataJson, inputStructureJson, dirForProjectCopy);
        projectAnalizer.analiseProject();

    }

//    private static FollowedData getInputPoints() {
//        return new FollowedData("JavaFileReader" , "analise" , "data");
//    }
}
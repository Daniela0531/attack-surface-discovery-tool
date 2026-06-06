package com.example;
import com.example.all_traces.AnalyzeAllInputStructures;
import com.example.entry_points.EntryPointsAnalyzer;
import com.example.generators.ClassToJsonWriter;
import com.example.structure.StructureSpoon;

import java.io.IOException;
import java.nio.file.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
//    private static Path dirForProjectCopy = Paths.get("preproccesed_project/keycloak/keycloak/admin/client");
    private static Path dirForProjectCopy = Paths.get("preproccesed_project");
//    services
    public static void main(String[] args) throws Exception {
        String pathToProject = "/Users/daniela/Desktop/5_year/2_sem/scalable_distribute_systems/java_scalable_distribute_systems";

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

        System.out.println("Построение структуры проекта ...");
        long start = 0;
        long end = 0;
        StructureSpoon structureSpoon = new StructureSpoon(dirForProjectCopy);

        String inputDatumJson = "project_structure/data.json";
//        String inputStructureJson = "project_structure/structure.json";
//        ProjectAnalizer projectAnalizer = new ProjectAnalizer(inputDatumJson, inputStructureJson, dirForProjectCopy);
//        ResultStructureNode resultStructureNode = projectAnalizer.analiseProject();
//        System.out.println(":::::::::::::::::::::::::::::::::::::::::");
//        resultStructureNode.print(0);
//        System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation());
//        CtModel structure = null;
//        try {
//            structure = structureBuilderSpoon.createProjectStructure();
//            System.out.println("Пострроение структуры завершено!");
//        } catch (IOException e) {
//            System.out.println("Ошибка пострроение структуры проекта");
//            return;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }



//        Path sourceDir = Paths.get("preproccesed_project");
//        String jsonOutputPath = "cpg_graph.json";
//

        start = System.nanoTime();

        structureSpoon.initSpoon();
        structureSpoon.initCpgGraph();
//
//        System.out.println("Анализ структуры проекта ...");
//        AnalyzerAfterSpoonForDatum analizerAfterSpoon = new AnalyzerAfterSpoonForDatum(structureSpoon, inputDatumJson);
//        ResultGraph resultNode = analizerAfterSpoon.analyzeDatumAndGetResult();
//        System.out.println(":::::::::::::::::::::::::::::::::::::::::");
//        resultNode.print();
//        System.out.println("Анализ структуры проекта завершён!");

//        String inputJson = "project_structure/input.json";
//        System.out.println("Анализ структуры проекта ...");
//        CtParameter<?> parameter = JsonToClassGenerator.createInputStructureFromJson(inputDatumJson, structureSpoon.getModel());
//        FollowedDatum startFollowedDatum = new MethodArgument(parameter);
//        NewAnalyzer analizerAfterSpoon = new NewAnalyzer(structureSpoon);
//        ResultGraph resultGraph = analizerAfterSpoon.analyzeDatumAndGetResult(startFollowedDatum);
//        System.out.println(":::::::::::::::::::::::::::::::::::::::::");
//        System.out.println("============== результат анализа =============");
//        resultGraph.print();
//        System.out.println("Анализ структуры проекта завершён!");
        end = System.nanoTime();
        System.out.println("Структура проекта построена за :: ");
        System.out.println("Время: " + (end - start) / 1_000_000 + " мс");
        System.out.println("============== анализ всех трасс =============");
        start = System.nanoTime();
        AnalyzeAllInputStructures analyzeAllInputStructures = new AnalyzeAllInputStructures();

//        analyzeAllInputStructures.buildAllTraces(structureSpoon);
        EntryPointsAnalyzer entryPointsAnalyzer = new EntryPointsAnalyzer();
        analyzeAllInputStructures.buildTracesForEntryPoints(entryPointsAnalyzer.findEntryPoints(structureSpoon), structureSpoon);

        analyzeAllInputStructures.print();
        end = System.nanoTime();
        System.out.println("Анализ структуры проекта завершён за :: ");
        System.out.println("Время: " + (end - start) / 1_000_000 + " мс");
//        System.out.println("============== результат анализа =============");
//        start = System.nanoTime();
//        analyzeAllInputStructures.print();
//        System.out.println("============== конец результата =============");
//        end = System.nanoTime();
//        System.out.println("Вывод результата за :: ");
//        System.out.println("Время: " + (end - start) / 1_000_000 + " мс");
        Path resultJson = Paths.get("/Users/daniela/Desktop/maga_diplom/repo/attack_surface_search/src/main/result.json");
        Files.write(Paths.get(resultJson.toUri()), "".getBytes());
        ClassToJsonWriter classToJsonWriter = new ClassToJsonWriter(resultJson);
        classToJsonWriter.writeToJson(analyzeAllInputStructures.getAllResults());
    }

//    private static FollowedDatum getInputPoints() {
//        return new FollowedDatum("JavaFileReader" , "analise" , "data");
//    }
}
package com.example;
import com.example.all_traces.AnalyzeAllInputStructures;
import com.example.entry_points.EntryPoint;
import com.example.entry_points.EntryPointsAnalyzer;
import com.example.entry_points.InputEntryPoint;
import com.example.generators.ClassToJsonWriter;
import com.example.generators.JsonToClassGenerator;
import com.example.structure.StructureSpoon;
import spoon.reflect.declaration.CtExecutable;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
//    private static Path dirForProjectCopy = Paths.get("preproccesed_project/keycloak/keycloak/admin/client");
    private static Path dirForProjectCopy = Paths.get("preproccesed_project");
//    services
    public static void main(String[] args) throws Exception {
        String pathToProject = "/Users/daniela/Desktop/5_year/1_sem/design";
        pathToProject = "/Users/daniela/Desktop/keycloack/keycloak/services";
        pathToProject = "/Users/daniela/Desktop/maga_diplom/main_test";

        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите абсолютный путь до локальной папки компьютера: ");
        pathToProject = scanner.nextLine(); // Читает всю строку до переноса

        scanner.close(); // Всегда закрывайте Scanner

        // копирование проекта в вспомогательную директорию

        System.out.println("Клонирую проект ...");
        ProjectCopyPreparation projectCopyPreparation = new ProjectCopyPreparation();
        Path sourcePath = Paths.get(pathToProject);
        try {
            projectCopyPreparation.projectCopyPreparation(sourcePath , dirForProjectCopy);
        } catch (IOException exception) {
            System.err.println("Ошибка при обработке файла");
            return;
        }

        System.out.println("Создание клона проекта завершено!");

        String pathToEntryPoints = "";
//        pathToEntryPoints = "entry_points/entry_points.json";

        System.out.println("Построение структуры проекта ...");
        long start = 0;
        long end = 0;

        start = System.nanoTime();
        StructureSpoon structureSpoon = new StructureSpoon(dirForProjectCopy);
        structureSpoon.initSpoonAndModel();
        structureSpoon.initCpgGraph();
        end = System.nanoTime();
        System.out.println("Структура проекта построена за :: ");
        System.out.println("Время: " + (end - start) / 1_000_000 + " мс");
        System.out.println("============== анализ всех трасс =============");

//        System.out.println("Обрабатываю файл с точками входа ...");
//        Path sourcePathEntryPoints = Paths.get(pathToEntryPoints);
        List<EntryPoint> entryPoints = new ArrayList<>();
//        try {
//            InputEntryPoint[] inputEntryPoints = JsonToClassGenerator.createEntryPointsFromJson(pathToEntryPoints);
//            if (inputEntryPoints.length > 0) {
//                for (InputEntryPoint inputEntryPoint : inputEntryPoints) {
//                    CtExecutable<?> ctExecutable = structureSpoon.getCtExecutableByFullSignature(inputEntryPoint.getSignature());
//                    entryPoints.add(new EntryPoint(ctExecutable, inputEntryPoint.getPositionInMethod()));
//                }
//            }
//        } catch (IOException exception) {
//            System.err.println("Ошибка при обработке файла");
//            return;
//        }
//        System.out.println("Обработка файла завершена!");

        AnalyzeAllInputStructures analyzeAllInputStructures = new AnalyzeAllInputStructures();

        EntryPointsAnalyzer entryPointsAnalyzer = new EntryPointsAnalyzer();
        start = System.nanoTime();
        if (pathToEntryPoints == "")
            entryPoints = entryPointsAnalyzer.findEntryPoints(structureSpoon);
        end = System.nanoTime();
        System.out.println("Анализ входных точек проекта завершён за :: ");
        System.out.println("Время: " + (end - start) + " мкс");

        start = System.nanoTime();
        analyzeAllInputStructures.buildTracesForEntryPoints(entryPoints, structureSpoon);
        end = System.nanoTime();

//        analyzeAllInputStructures.print();
        System.out.println("Анализ структуры проекта завершён за :: ");
        System.out.println("Время: " + (end - start) / 1_000_000 + " мс");

        analyzeAllInputStructures.print();

        Path resultJson = Paths.get("/Users/daniela/Desktop/maga_diplom/repo/attack_surface_search/src/main/result.json");
        Files.write(Paths.get(resultJson.toUri()), "".getBytes());
        ClassToJsonWriter classToJsonWriter = new ClassToJsonWriter(resultJson);
        classToJsonWriter.writeToJson(analyzeAllInputStructures.getAllResults());
    }

}
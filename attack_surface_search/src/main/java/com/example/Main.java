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
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
    private static void printHelp() {
        System.out.println("Attack Surface Discovery Tool");
        System.out.println();
        System.out.println("Инструмент статического анализа Java-проектов.");
        System.out.println("Строит граф вызовов, находит точки входа и трассирует пути распространения данных.");
        System.out.println();
        System.out.println("Использование:");
        System.out.println("  java -jar attack_surface_search.jar [--path <путь>] [--out <файл>] [--help]");
        System.out.println();
        System.out.println("Флаги:");
        System.out.println("  --path, -p    Путь до папки с исходниками анализируемого Java-проекта");
        System.out.println("  --out,  -o    Путь к выходному JSON-файлу (по умолчанию: result.json)");
        System.out.println("  --help, -h    Показать это сообщение");
        System.out.println();
        System.out.println("Что делает программа:");
        System.out.println("  1. Копирует исходники в рабочую директорию preproccesed_project/");
        System.out.println("  2. Строит модель проекта через Spoon (AST + граф вызовов)");
        System.out.println("  3. Находит точки входа (entry points)");
        System.out.println("  4. Трассирует пути для каждой точки входа");
        System.out.println("  5. Выводит результат в консоль и записывает в указанный JSON-файл");
        System.out.println();
        System.out.println("Примеры:");
        System.out.println("  java -jar attack_surface_search.jar --path C:\\projects\\my-app\\src");
        System.out.println("  java -jar attack_surface_search.jar -p C:\\projects\\my-app\\src -o C:\\results\\out.json");
        System.out.println("  java -jar attack_surface_search.jar   (интерактивный ввод пути)");
    }

    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        for (String arg : args) {
            if (arg.equals("--help") || arg.equals("-h")) {
                printHelp();
                return;
            }
        }

        String pathToProject = null;
        String pathToOutputName = "result";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--path") || args[i].equals("-p")) {
                if (i + 1 >= args.length) {
                    System.err.println("Ошибка: флаг " + args[i] + " требует значение (путь).");
                    printHelp();
                    return;
                }
                pathToProject = args[i + 1];
            } else if (args[i].equals("--out") || args[i].equals("-o")) {
                if (i + 1 >= args.length) {
                    System.err.println("Ошибка: флаг " + args[i] + " требует значение (путь к файлу).");
                    printHelp();
                    return;
                }
                pathToOutputName = args[i + 1];
            }
        }

        if (pathToProject == null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите абсолютный путь до локальной папки компьютера: ");
            pathToProject = scanner.nextLine();
            scanner.close();
        }

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
        System.out.println("[DEBUG] Запуск buildModel (Spoon) ...");
        structureSpoon.initSpoonAndModel();
        System.out.println("[DEBUG] buildModel завершён. Запуск initCpgGraph ...");
        structureSpoon.initCpgGraph();
        System.out.println("[DEBUG] initCpgGraph завершён.");
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

//        Path resultJson = Paths.get(pathToOutputName + ".json");
//        Files.createDirectories(resultJson.toAbsolutePath().getParent());
//        Files.write(resultJson, "".getBytes());
//        System.out.println("Результат будет записан в: " + resultJson.toAbsolutePath());
//        ClassToJsonWriter classToJsonWriter = new ClassToJsonWriter(resultJson);
//        classToJsonWriter.writeToJson(analyzeAllInputStructures.getAllResults());

        pathToOutputName = "result";
        int n = analyzeAllInputStructures.getAllResults().size(); // количество файлов
        Path resultJson = Paths.get(pathToOutputName + ".json");
//        Files.createDirectories(resultJson.toAbsolutePath().getParent());
        String outputDir = "./results";
//
//        // Создаем папку
        Files.createDirectories(Paths.get(outputDir));

        // Создаем файлы
        for (int i = 0; i < n; ++i) {
            Path file = Paths.get(outputDir, "result" + i + ".json");
            Files.write(file, "".getBytes());
//            System.out.println("Результат будет записан в: " + resultJson.toAbsolutePath());
            ClassToJsonWriter classToJsonWriter = new ClassToJsonWriter(file);
            classToJsonWriter.writeToJson(analyzeAllInputStructures.getAllResults().get(i));
        }
    }

}
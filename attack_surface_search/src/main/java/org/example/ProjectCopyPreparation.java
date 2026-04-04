package org.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public class ProjectCopyPreparation {
    public void projectCopyPreparation(Path pathToProject , Path pathToDirForProjectCopy) throws IOException {

        try {
            // 1. Подготовка папки (удаление старой или создание новой)
            prepareDirectory(pathToDirForProjectCopy);

            // 2. нужно добавить обработку типа архива: jar , zip , ...

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<Path> stream = Files.walk(pathToProject)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(file -> {
                        try {
                            // Вычисляем путь внутри новой папки
                            // вычисляем относительный путь до файла
                            Path relativePath = pathToProject.relativize(file);
                            // вычисляем новый абсолютный путь до файла в целевой папке
                            Path destination = pathToDirForProjectCopy.resolve(relativePath);

                            // Создаем подпапки
                            Files.createDirectories(destination.getParent());

                            // Читаем все содержимое файла
                            try (Stream<String> lines = Files.lines(file); BufferedWriter writer = Files.newBufferedWriter(destination)) {
                                lines.forEach(line -> {
                                    // Логика обработки одной строки
                                    try {
                                        writer.write(line);
                                        writer.newLine();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } catch (IOException e) {
                                System.out.println("я не смогла прочитать файл");
                            }
                            // Записываем результат

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private static void prepareDirectory(Path dir) throws IOException {
        if (Files.exists(dir)) {
            // Рекурсивное удаление содержимого
            Files.walkFileTree(dir , new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file , BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path d , IOException exc) throws IOException {
                    if (!d.equals(dir)) Files.delete(d);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            Files.createDirectories(dir);
        }
    }

}

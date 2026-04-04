package org.example.analizer;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;
public class JavaFileReader {
    public void analise(Path projectPath, FollowedData data) {

        try (Stream<Path> paths = Files.walk(projectPath)) {
            paths
                    // Фильтруем: только файлы , заканчивающиеся на .java
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        System.out.println("--- Читаем файл: " + path.getFileName() + " ---");
                        try {
                            // Читаем всё содержимое файла и выводим в консоль
                            List<String> content = Files.readAllLines(path);
                            int size = content.size();
                            CodeAnaliser codeAnaliser = new CodeAnaliser(size);
                            System.out.println("file lines count = " + size+ "\n");

                            codeAnaliser.analiseOneFile(content , data);
                            codeAnaliser.print(data);
                        } catch (IOException e) {
                            System.err.println("Ошибка при чтении файла " + path + ": " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

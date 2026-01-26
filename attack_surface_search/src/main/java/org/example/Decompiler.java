package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Decompiler {
    Integer decompile(String jarName) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        ProcessBuilder builder = new ProcessBuilder();

        if (isWindows) {
            builder.command("cmd.exe", "/c", "dir");
        } else {
            builder.command("jadx", "-d", "decompiled_folder", jarName);
        }

        try {
            // Запускаем процесс
            Process process = builder.start();

            // Читаем стандартный вывод команды
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Ждем завершения процесса и получаем код выхода
            int exitCode = process.waitFor();
            return exitCode;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

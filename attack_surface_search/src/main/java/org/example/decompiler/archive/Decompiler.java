package org.example.decompiler.archive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Decompiler {
//    public void printFunc(String text) {
//            System.out.println("I am a help func" + text);
//    }
    public String testString;
    String decompile(String jarName) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        ProcessBuilder builder = new ProcessBuilder();

        if (isWindows) {
            builder.command("cmd.exe" , "/c" , "dir");
        } else {
            builder.command("jadx" , "-d" , "decompiled_folder" , jarName);
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
            return "decompiled_folder";

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

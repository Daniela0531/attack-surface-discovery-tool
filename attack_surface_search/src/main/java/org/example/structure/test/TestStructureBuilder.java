package org.example.structure.test;

import org.example.structure.ProjectStructure;
import org.example.structure.StructureBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestStructureBuilder {
//    private ProjectPreprocessor projectPreprocessor;

    public static void main(String[] args) throws Exception {
        String pathToProject = "/Users/daniela/Desktop/maga_diplom/repo/attack_surface_search/src/main/java/org/example/structure/test";
//        ProjectCopyPreparation projectCopyPreparation = new ProjectCopyPreparation();
        Path path = Paths.get(pathToProject);
//
        StructureBuilder structureBuilder = new StructureBuilder(path);
//
//        String content = Files.readString(path);
//        String[] strings = content.split("\\R");
//        for (String str : strings) {
//            System.out.println(str);
//
//            String methodRegex = "(|public|private|protected|static|\\s+)([\\w\\<\\>\\,\\[\\]]+)\\s+(\\w+)\\s*\\(";
//
//            Pattern pattern = Pattern.compile(methodRegex);
//            Matcher matcher = pattern.matcher(str);
//
//            if (matcher.find()) {
//                String methodName = matcher.group(3);
////                String rawArgs = matcher.group(4).trim();
//
//                System.out.println("Метод: " + methodName);
//            }
//        }

        ProjectStructure projectStructure = structureBuilder.createProjectStructure();
//        projectStructure.print();
    }
}

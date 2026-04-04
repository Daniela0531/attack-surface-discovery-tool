package org.example.text_preparing;

import org.example.ProjectCopyPreparation;
import org.example.text_preparing.ProjectPreprocessor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPreprocessing {
//    private ProjectPreprocessor projectPreprocessor;

    public static void main(String[] args) throws IOException {
        String pathToProject = "/Users/daniela/Desktop/maga_diplom/repo/attack_surface_search/src/main/java/org/example/text_preparing/test";
//        ProjectCopyPreparation projectCopyPreparation = new ProjectCopyPreparation();
        Path path = Paths.get(pathToProject);

        ProjectPreprocessor projectPreprocessor = new ProjectPreprocessor(path);
        projectPreprocessor.processProject();
    }
}

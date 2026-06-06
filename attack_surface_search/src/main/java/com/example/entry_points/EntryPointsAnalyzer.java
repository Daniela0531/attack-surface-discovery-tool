package com.example.entry_points;

import com.example.auxiliary_functions.Functions;
import com.example.structure.StructureSpoon;
import com.example.structure.graph.CpgGraph;
import com.example.structure.graph.Edge;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class EntryPointsAnalyzer {

    // Список аннотаций, характерных для REST API методов
    private static final Set<String> REST_METHOD_ANNOTATIONS = Set.of(
            // JAX-RS / Jakarta EE
            "jakarta.ws.rs.GET", "jakarta.ws.rs.POST", "jakarta.ws.rs.PUT",
            "jakarta.ws.rs.DELETE", "jakarta.ws.rs.PATCH", "jakarta.ws.rs.Path",
            // Spring Framework
            "org.springframework.web.bind.annotation.RequestMapping",
            "org.springframework.web.bind.annotation.GetMapping",
            "org.springframework.web.bind.annotation.PostMapping",
            "org.springframework.web.bind.annotation.PutMapping",
            "org.springframework.web.bind.annotation.DeleteMapping",
            "org.springframework.web.bind.annotation.PatchMapping"
    );

    // Список аннотаций класса, которые делают все его методы эндпоинтами
    private static final Set<String> REST_CLASS_ANNOTATIONS = Set.of(
            "org.springframework.web.bind.annotation.RestController",
            "jakarta.ws.rs.Path"
    );

    public List<CtExecutable> findEntryPoints(StructureSpoon structureSpoon) {
        CpgGraph graph = structureSpoon.getGraph();
        List<CtExecutable> result = new ArrayList<>();
        for (CtExecutable ctExecutable : graph.getNodes()) {
            boolean methodHasRestAnnotation = ctExecutable.getAnnotations().stream()
                    .anyMatch(annotation -> REST_METHOD_ANNOTATIONS.contains(annotation.getType().getQualifiedName()));

            if (!methodHasRestAnnotation) {
                // Если метод не аннотирован, проверяем, не находится ли он в REST-классе
                CtType<?> declaringType = ctExecutable.getParent(CtType.class);
                if (declaringType != null) {
                    boolean classHasRestAnnotation = declaringType.getAnnotations().stream()
                            .anyMatch(annotation -> REST_CLASS_ANNOTATIONS.contains(annotation.getType().getQualifiedName()));

                    // Если класс контроллер и метод публичный — считаем его эндпоинтом
                    // (Это эвристика, можно уточнить по другим критериям)
                    if (classHasRestAnnotation && ctExecutable instanceof CtMethod) {
                        methodHasRestAnnotation = true;
                    }
                }

            }

            if (ctExecutable.getSimpleName().equals("health")) {
                System.out.println("health");
            }

            if (methodHasRestAnnotation) {
                if (ctExecutable.getSimpleName().equals("health")) {
                    System.out.println("health with right annotation");
                }
                result.add(ctExecutable);
            }
        }
        return result;
    }

}


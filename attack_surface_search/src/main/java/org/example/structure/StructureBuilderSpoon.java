package org.example.structure;

import spoon.Launcher;
import spoon.reflect.CtModel;

import java.nio.file.Path;
import java.nio.file.Paths;


public class StructureBuilderSpoon {
    private Path source;
    private Path target = Paths.get("project_structure/structure.json");
//    private String vocabulary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
    private CtModel model;
    public StructureBuilderSpoon(Path source) {
        this.source = source;
    }

    public CtModel createProjectStructure() throws Exception {
        Launcher launcher = new Launcher();

        // Указываем путь к исходникам вашего проекта
        launcher.addInputResource(source.toRealPath().toString());

        // 1. Разрешаем работу без внешних библиотек
        launcher.getEnvironment().setNoClasspath(true);
        // 2. Позволяем Spoon пытаться угадывать типы (Type Inference)
        // Это поможет частично восстановить информацию о пакетах из импортов
        launcher.getEnvironment().setIgnoreSyntaxErrors(true);
        // 3. Автоматическая обработка импортов (нужна для Pretty Printing и анализа)
        launcher.getEnvironment().setAutoImports(true);
        // 4. Поддержка комментариев (помогает, если инфо о методах есть в Javadoc)
        launcher.getEnvironment().setCommentEnabled(true);

        // Построение модели
        launcher.buildModel();
        this.model = launcher.getModel();

        return model;
        // Вывод всех классов проекта
//        model.getAllTypes().forEach(type -> System.out.println("Класс: " + type.getQualifiedName()));
//        model.getAllPackages().forEach(type -> System.out.println("Пакет: " + type.getQualifiedName()));
//        model.getAllModules().forEach(type -> System.out.println("Module: " + type.getSimpleName() + " annotations:" + type.getPosition()));
    }


}

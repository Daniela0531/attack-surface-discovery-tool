package org.example.structure;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StructureBuilder {
    private Path source;
    private Path target = Paths.get("project_structure/structure.json");
    private String vocabulary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
    public StructureBuilder(Path source) {
        this.source = source;
    }
    private boolean parseOperandName(String str) {
        String regex = "^[" + vocabulary + "]+$";
        return str.matches(regex);
    }
    public ProjectStructure createProjectStructure() throws Exception {
        ProjectStructure projectStructure = new ProjectStructure();
        StructureEntity parentEntity = new StructureEntity("PACKAGE", source, 0);
        parentEntity.setName(source.toString());

        // создание файловой структуры
        createPackagesStructure(parentEntity);

        // склеиваем лишние папки
        // Очередь для хранения папок, которые нужно посетить
        Deque<StructureEntity> queueEntities = new ArrayDeque<>();
        queueEntities.add(parentEntity);

        while (!queueEntities.isEmpty()) {
            // извлекаем и удаляем первый элемент
            StructureEntity curEntity = queueEntities.poll();
            if (Objects.equals(curEntity.getType(), "FILE")) {
                try (Stream<Path> stream = Files.walk(curEntity.getPath())) {
                    stream.filter(Files::isRegularFile)
                            .filter(path -> path.toString().endsWith(".java"))
                            .forEach(file -> {
                                try {
                                    findMethodsAndSubClasses(curEntity);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }
            } else if (curEntity.getChildren().size() == 1 && Objects.equals(curEntity.getChildren().get(0).getType(), "PACKAGE")) {
                StructureEntity child = curEntity.getChildren().get(0);
                curEntity.setPath(child.getPath());
                curEntity.setChildren(child.getChildren());
                for (StructureEntity newChild : child.getChildren())
                    newChild.setLevel(curEntity.getLevel() + 1);
                queueEntities.add(curEntity);
            } else {
                for (StructureEntity entity : curEntity.getChildren()) {
                    entity.setLevel(curEntity.getLevel() + 1);
                    queueEntities.add(entity);
                }
            }
        }

        projectStructure.setMainParent(parentEntity);
        ClassToJsonWriter toJsonWriter = new ClassToJsonWriter(target);
        toJsonWriter.writeToJson(parentEntity);

        return projectStructure;
    }

    private void createPackagesStructure(StructureEntity parentEntity) {
        // Очередь для хранения папок, которые нужно посетить
        Deque<Path> queue = new ArrayDeque<>();
        queue.add(source);
        Deque<StructureEntity> queuePackages = new ArrayDeque<>();
        queuePackages.add(parentEntity);

        while (!queue.isEmpty()) {
            // извлекаем и удаляем первый элемент
            Path currentDir = queue.poll();
            StructureEntity curEntity = queuePackages.poll();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir)) {
                for (Path entry : stream) {
                    // Если это папка, добавляем её в конец очереди для последующего захода
                    if (Files.isDirectory(entry)) {
                        queue.add(entry);
                        StructureEntity childEntity = new StructureEntity("PACKAGE", entry, curEntity.getLevel() + 1);
                        childEntity.setName(entry.toString());
                        curEntity.addChild(childEntity);
                        childEntity.setParent(curEntity);
                        queuePackages.add(childEntity);

                    } else {
                        StructureEntity childEntity = new StructureEntity("FILE", entry, curEntity.getLevel() + 1);
                        curEntity.addChild(childEntity);
                        childEntity.setParent(curEntity);

                        String path = entry.toString();
                        Pattern p = Pattern.compile("[^\\\\/]+$");
                        Matcher m = p.matcher(path);

                        if (m.find()) {
                            childEntity.setName(m.group());
                        }

                    }
                }

            } catch (IOException e) {
                System.err.println("Ошибка доступа к " + currentDir + ": " + e.getMessage());
            }
        }
    }

    private void findMethodsAndSubClasses(StructureEntity curEntity) throws IOException {
        String content = Files.readString(curEntity.getPath());
        String[] strings = content.split("\\R");
        int curScopeDepth = 0;
        int requiedScopeDepthForClass = 0;
        Stack<String> stackForCheckingIfBracketsBalanced = new Stack<>();
        boolean methodIsStarts = false;
//        boolean methodIsEnds = true;

        for (String str : strings) {
            // обработка области видимости
            // Если скобка открывающая — кладем в стек
            if (Objects.equals(str, "{")) {
                stackForCheckingIfBracketsBalanced.push(str);
                ++curScopeDepth;
            }
            // Если закрывающая — проверяем соответствие, закрылась область видимости
            else if (Objects.equals(str, "}")) {
                if (stackForCheckingIfBracketsBalanced.isEmpty()) {
                    System.out.println("Плохой код или неверный препроцессинг! пустой стек");
                    throw new IOException("неверное количество скобок");
                }

                String top = stackForCheckingIfBracketsBalanced.pop();
                // иначе -> изменилась область видимости (вышли в родительскую)
                --curScopeDepth;
            }

            if (requiedScopeDepthForClass > curScopeDepth) {
                curEntity = curEntity.getParent();
                --requiedScopeDepthForClass;
            }

            String[] tokens = str.split("\\s+");

            for (int i = 0; i < tokens.length; ++i) {
                // обработка
                if (tokens[i].equals("class")) {
                    requiedScopeDepthForClass = curScopeDepth;
                    StructureEntity childClass = new StructureEntity("CLASS", curEntity.getPath(), curEntity.getLevel() + 1);
                    childClass.setName(tokens[i + 1]);
                    childClass.setParent(curEntity);
                    curEntity.addChild(childClass);
                    curEntity = childClass;
                } else if (tokens[i].equals("interface")) {
                    requiedScopeDepthForClass = curScopeDepth;
                    StructureEntity childClass = new StructureEntity("INTERFACE", curEntity.getPath(), curEntity.getLevel() + 1);
                    childClass.setName(tokens[i + 1]);
                    childClass.setParent(curEntity);
                    curEntity.addChild(childClass);
                    curEntity = childClass;
                } else if (tokens[i].equals("enum")) {
                    requiedScopeDepthForClass = curScopeDepth;
                    StructureEntity childClass = new StructureEntity("ENUM", curEntity.getPath(), curEntity.getLevel() + 1);
                    childClass.setName(tokens[i + 1]);
                    childClass.setParent(curEntity);
                    curEntity.addChild(childClass);
                    curEntity = childClass;
                }
            }

            if (curScopeDepth == requiedScopeDepthForClass + 1) {
                String methodRegex = "^\\s*(?:(?:public|private|protected|static|final|static)\\s+)*([\\w\\<\\>\\,\\[\\]]+)\\s+(\\w+)\\s*\\(([^)]*)\\);?";

                Pattern pattern = Pattern.compile(methodRegex);
                Matcher matcher = pattern.matcher(str);

                if (matcher.matches()) {
                    methodIsStarts = true;
                    StructureEntity method = new StructureEntity("METHOD", curEntity.getPath(), curEntity.getLevel() + 1);
                    int size = matcher.groupCount();
                    String methodName = matcher.group(size - 1);
                    method.setName(methodName);
                    String rawArgs = matcher.group(size).trim();
                    if (rawArgs.isEmpty()) {
                        method.setChildren(new ArrayList<>());
                    } else {
                        // Сплитим аргументы по запятой, игнорируя запятые внутри < >
                        String[] argsArray = rawArgs.split(",\\s*(?![^<]*>)");

//                    System.out.println("  Аргументы: [");
                        for (int j = 0; j < argsArray.length; ++j) {
                            ArrayList<String> argumentInfo = new ArrayList<>(List.of(argsArray[j].split(" ")));
                            String argumentName = argumentInfo.remove(argumentInfo.size() - 1);
                            String argumentType = String.join(" ", argumentInfo);
                            // Разделяем тип и имя (последнее слово - имя, всё до него - тип)
//                        int lastSpace = arg.lastIndexOf(" ");
//                        String type = arg.substring(0, lastSpace).trim();
//                        String name = arg.substring(lastSpace).trim();

                            StructureEntity argument = new StructureEntity("ARGUMENT", method.getPath(), method.getLevel() + 1);
                            argument.setName(argumentName);
                            argument.setParent(method);
                            method.addChild(argument);
                        }
                    }
                    method.setParent(curEntity);
                    curEntity.addChild(method);
                } else {
                    // поля класса ??
                    // что ещё может быть ??
                }
            } else if (curScopeDepth > requiedScopeDepthForClass) {
                if (methodIsStarts) {
                    curEntity.getContent().add(str);
                }
            } else if (curScopeDepth == requiedScopeDepthForClass) {
                if (methodIsStarts) {
                    methodIsStarts = false;
                }
            }
        }
    }
}

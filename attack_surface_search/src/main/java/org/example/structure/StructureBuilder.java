package org.example.structure;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
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
    public StructureBuilder(Path source) {
        this.source = source;
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
        ToJsonWriter toJsonWriter = new ToJsonWriter(target);
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

    private StructureEntity findMethodsAndSubClasses(StructureEntity curEntity) throws IOException {
        String content = Files.readString(curEntity.getPath());
        String[] strings = content.split("\\R");
        int curScopeDepth = 0;
        int requiedScopeDepth = 0;
        Stack<String> stackForCheckingIfBracketsBalanced = new Stack<>();

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

            if (requiedScopeDepth > curScopeDepth) {
                curEntity = curEntity.getParent();
                --requiedScopeDepth;
            }

            String[] tokens = str.split("\\s+");

            for (int i = 0; i < tokens.length; ++i) {
                // обработка
                if (tokens[i].equals("class")) {
                    requiedScopeDepth = curScopeDepth;
                    StructureEntity childClass = new StructureEntity("CLASS", curEntity.getPath(), curEntity.getLevel() + 1);
                    childClass.setName(tokens[i + 1]);
                    childClass.setParent(curEntity);
                    curEntity.addChild(childClass);
                    curEntity = childClass;
                } else if (tokens[i].equals("interface")) {
                    requiedScopeDepth = curScopeDepth;
                    StructureEntity childClass = new StructureEntity("INTERFACE", curEntity.getPath(), curEntity.getLevel() + 1);
                    childClass.setName(tokens[i + 1]);
                    childClass.setParent(curEntity);
                    curEntity.addChild(childClass);
                    curEntity = childClass;
                } else if (tokens[i].equals("enum")) {
                    requiedScopeDepth = curScopeDepth;
                    StructureEntity childClass = new StructureEntity("ENUM", curEntity.getPath(), curEntity.getLevel() + 1);
                    childClass.setName(tokens[i + 1]);
                    childClass.setParent(curEntity);
                    curEntity.addChild(childClass);
                    curEntity = childClass;
                }
            }
            if (str.equals("boolean isFinished ( );")) {
                System.out.println("boolean isFinished ( );");
                System.out.println("curScopeDepth : " + curScopeDepth);
                System.out.println("requiedScopeDepth : " + requiedScopeDepth);
            }

            if (curScopeDepth == requiedScopeDepth + 1) {
                String methodRegex = "^\\s*(?:(?:public|private|protected|static|final|static)\\s+)*([\\w\\<\\>\\,\\[\\]]+)\\s+(\\w+)\\s*\\(([^)]*)\\);?";

                Pattern pattern = Pattern.compile(methodRegex);
                Matcher matcher = pattern.matcher(str);

                if (matcher.matches()) {
                    if (str.equals("boolean isFinished ( );")) {
                        System.out.println("matchers : " + matcher.group());
                    }
                    StructureEntity method = new StructureEntity("METHOD", curEntity.getPath(), curEntity.getLevel() + 1);
                    int size = matcher.groupCount();
                    String methodName = matcher.group(size - 1);
                    method.setName(methodName);
                    String rawArgs = matcher.group(size).trim();
//                    System.out.println("method ::: " + method.getName());
//                    System.out.println("from string ::: " + matcher.group());
//                    System.out.println(rawArgs);
                    if (rawArgs.isEmpty()) {
//                    System.out.println("  Аргументы: [нет]");
                        method.setChildren(new ArrayList<>());
                    } else {
                        // Сплитим аргументы по запятой, игнорируя запятые внутри < >
                        String[] argsArray = rawArgs.split(",\\s*(?![^<]*>)");
//                    System.out.println("  Количество аргументов: " + argsArray.length);

//                    System.out.println("  Аргументы: [");
                        for (int j = 0; j < argsArray.length; ++j) {
//                        System.out.println(argsArray[j]);
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
                }
            }
        }
        return curEntity;
    }
}


// внутренний класс
// использование:
// вар 1:
//public class Outer {
//    private String secret = "Пароль";
//
//    class Inner {
//        void show() {
//            // Имеет доступ к приватным полям Outer
//            System.out.println("Доступ к: " + secret);
//        }
//    }
//}
//
//    // Использование:
//    Outer out = new Outer();
//    Outer.Inner in = out.new Inner(); // Создаем через объект out
//in.show();
// вар 2:
//public class Outer {
//    static class Nested {
//        void msg() {
//            System.out.println("Я независим от объектов Outer");
//        }
//    }
//}
//
//    // Использование:
//    Outer.Nested n = new Outer.Nested(); // Объект Outer не нужен
//n.msg();
// вар 3
//public class Outer {
//    void process() {
//        class Local {
//            void doWork() { System.out.println("Работаю внутри метода"); }
//        }
//        Local l = new Local();
//        l.doWork();
//    }
//}
//вар 4
//Runnable r = new Runnable() {
//    @Override
//    public void run() {
//        System.out.println("Анонимный запуск");
//    }
//};
//
// второй класс - находится в одном файле и не может быть использован за пределами пакета
// оформляется также, но не имеет ключа public



//    Stack<String> tokensStack = new Stack<>();
//    Stack<String> stackForCheckingIfBracketsBalanced = new Stack<>();
//
//        for (String token : tokens) {
//                // Если скобка открывающая — кладем в стек
//                if (Objects.equals(token, "(") || Objects.equals(token, "[") || Objects.equals(token, "{")) {
//                    stackForCheckingIfBracketsBalanced.push(token);
//                }
//                // Если закрывающая — проверяем соответствие, закрылась область видимости
//                else if (Objects.equals(token, ")") || Objects.equals(token, "]") || Objects.equals(token, "}")) {
//                    if (stackForCheckingIfBracketsBalanced.isEmpty()) {
//                        System.out.println("Плохой код или неверный препроцессинг!");
//                        throw new IOException("неверное количество скобок");
//                    }
//
//                    String top = stackForCheckingIfBracketsBalanced.pop();
//                    if (Objects.equals(top, "(") && Objects.equals(token, ")") ||
//                        Objects.equals(top, "{") && Objects.equals(token, "}") ||
//                        Objects.equals(top, "[") && Objects.equals(token, "]"))  {
//                        System.out.println("Плохой код или неверный препроцессинг!");
//                        throw new IOException("неверное количество скобок");
//                    }
//                }
//        }


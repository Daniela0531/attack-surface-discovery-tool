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
//    public static void main(String[] args) throws IOException {
//        String pathToProject = "/Users/daniela/Desktop/maga_diplom/repo/attack_surface_search/src/main/java/org/example/structure/test";
////        ProjectCopyPreparation projectCopyPreparation = new ProjectCopyPreparation();
//        Path path = Paths.get(pathToProject);
////
//        StructureBuilder structureBuilder = new StructureBuilder(path);
////
////        String content = Files.readString(path);
////        String[] strings = content.split("\\R");
////        for (String str : strings) {
////            System.out.println(str);
////
////            String methodRegex = "(|public|private|protected|static|\\s+)([\\w\\<\\>\\,\\[\\]]+)\\s+(\\w+)\\s*\\(";
////
////            Pattern pattern = Pattern.compile(methodRegex);
////            Matcher matcher = pattern.matcher(str);
////
////            if (matcher.find()) {
////                String methodName = matcher.group(3);
//////                String rawArgs = matcher.group(4).trim();
////
////                System.out.println("Метод: " + methodName);
////            }
////        }
//
//        ProjectStructure projectStructure = structureBuilder.createProjectStructure();
//        projectStructure.print();
//    }
    public ProjectStructure createProjectStructure() throws Exception {
        ProjectStructure projectStructure = new ProjectStructure();
        StructureEntity parentEntity = new StructureEntity("PACKAGE", source, 0);
        parentEntity.setName(source.toString());

        String pathString = source.toString();
        Pattern p = Pattern.compile("\"([^\\\\\\\\/]+)[\\\\\\\\/]?$\"");
        Matcher m = p.matcher(pathString);

        if (m.find()) {
            parentEntity.setName(m.group(1));
        }

        // создание файловой структуры
        createPackagesStructure(parentEntity);

        // склеиваем лишние папки
        // Очередь для хранения папок, которые нужно посетить
        Deque<StructureEntity> queueEntities = new ArrayDeque<>();
        queueEntities.add(parentEntity);

        System.out.println("before");

        while (!queueEntities.isEmpty()) {
            // извлекаем и удаляем первый элемент
            StructureEntity curEntity = queueEntities.poll();
            if (Objects.equals(curEntity.getType(), "FILE")) {
                try (Stream<Path> stream = Files.walk(curEntity.getPath())) {
                    stream.filter(Files::isRegularFile)
                            .filter(path -> path.toString().endsWith(".java"))
                            .forEach(file -> {
                                try {
//                                    System.out.println("type before::: " + curEntity.getType());
//                                    System.out.println("after:: " + curEntity.getName());
                                    findMethodsAndSubClasses(curEntity);
                                    curEntity.print();
//                                    System.out.println("type after::: " + curEntity.getType());
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
//        projectStructure.setEntities(parentEntity.getChildren());

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

                        String path = entry.toString();
                        Pattern p = Pattern.compile("\"([^\\\\\\\\/]+)[\\\\\\\\/]?$\"");
                        Matcher m = p.matcher(path);

                        if (m.find()) {
                            childEntity.setName(m.group(1));
                        }

                    } else {
//                        System.out.println("file name::: " + entry.toString());
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
//        curEntity.addChild(new StructureEntity("TEST", curEntity.getPath(), curEntity.getLevel() + 1));
//        curEntity.print();
//        return null;
//        ArrayList<StructureEntity> children = new ArrayList<>();
//        System.out.println("parent before::: " + curEntity.getParent().getPath());
        String content = Files.readString(curEntity.getPath());
        String[] strings = content.split("\\R");
        int curScopeDepth = 0;
        int requiedScopeDepth = 0;
//        Stack<String> tokensStack = new Stack<>();
        Stack<String> stackForCheckingIfBracketsBalanced = new Stack<>();

        for (String str : strings) {
//            System.out.println("type ::: " + curEntity.getType());
//            System.out.println("name ::: " + curEntity.getName());
//            System.out.println(str);


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
//                        System.out.println(str);
                    throw new IOException("неверное количество скобок");
                }

                String top = stackForCheckingIfBracketsBalanced.pop();
//                    if (Objects.equals(top, "{") && Objects.equals(token, "}")) {
//                        System.out.println("Плохой код или неверный препроцессинг! не та скобка");
////                        System.out.println(str);
//                        throw new IOException("неверное количество скобок");
//                    }
                // иначе -> изменилась область видимости (вышли в родительскую)
                --curScopeDepth;
            }

            if (requiedScopeDepth > curScopeDepth) {
                curEntity = curEntity.getParent();
                --requiedScopeDepth;
            }


//                System.out.println("type ::: " + curEntity.getType());
//                System.out.println("name ::: " + curEntity.getName());
//                System.out.println("requiedScopeDepth ::: " + String.format("%d", requiedScopeDepth));
//                System.out.println("curScopeDepth ::: " + String.format("%d", curScopeDepth));

//                System.out.println(tokens[i]);
            String[] tokens = str.split("\\s+");
//            System.out.println("------------------------------- ");
//            curEntity.print();

            for (int i = 0; i < tokens.length; ++i) {

                String token = tokens[i];
                // обработка
                if (tokens[i].equals("class")) {
//                    if (Objects.equals(curEntity.getName(), "GameConfiguration.java")) {
                    System.out.println(str);
                    requiedScopeDepth = curScopeDepth;
                    System.out.println(curEntity.toString());
                    StructureEntity childClass = new StructureEntity("CLASS", curEntity.getPath(), curEntity.getLevel() + 1);
//                    System.out.println("class ::: " + childClass.getName());
                    childClass.setName(tokens[i + 1]);
                    System.out.println("class ::: " + childClass.getName());
                    childClass.setParent(curEntity);
                    System.out.println("class parent ::: " + childClass.getParent().getName());
                    curEntity.addChild(childClass);
                    System.out.println("parent children::: " + curEntity.getChildren().size());
                    curEntity = childClass;
                    System.out.println("cur ::: " + curEntity.getName());
                } else if (tokens[i].equals("interface")) {
                    requiedScopeDepth = curScopeDepth;
                    StructureEntity childClass = new StructureEntity("INTERFACE", curEntity.getPath(), curEntity.getLevel() + 1);
                    childClass.setName(tokens[i + 1]);
                    childClass.setParent(curEntity);
                    curEntity.addChild(childClass);
                    curEntity = childClass;
//                    System.out.println("::is interface::");
//                    System.out.println(curEntity.getParent().getPath());
//                    System.out.println("parent after::: " + curEntity.getParent().getPath());
                } else if (tokens[i].equals("ENUM")) {
                    requiedScopeDepth = curScopeDepth;
                    StructureEntity childClass = new StructureEntity("ENUM", curEntity.getPath(), curEntity.getLevel() + 1);
                    childClass.setName(tokens[i + 1]);
                    childClass.setParent(curEntity);
                    curEntity.addChild(childClass);
                    curEntity = childClass;
                }
//                System.out.println("parent after::: " + curEntity.getParent().getPath());
                //            if (requiedScopeDepth >= curScopeDepth)
                // поиск методов
            }
//            if (Objects.equals(curEntity.getName(), "GameConfiguration.java"))
//                System.out.println("class ::: " + curEntity.getName());
            String methodRegex = "(|public|private|protected|static|\\s+)([\\w\\<\\>\\,\\[\\]]+)\\s+(\\w+)\\s*\\(([^)]*)\\)";

            Pattern pattern = Pattern.compile(methodRegex);
            Matcher matcher = pattern.matcher(str);

//            if (Objects.equals(curEntity.getName(), "GameConfiguration.java"))
//                System.out.println("class ::: " + curEntity.getName());
            if (matcher.find()) {
//                    System.out.println("parent after::: " + curEntity.getParent().getPath());


                System.out.println("mathod !!!!");
                StructureEntity method = new StructureEntity("METHOD", curEntity.getPath(), curEntity.getLevel() + 1);
                String methodName = matcher.group(3);
                method.setName(methodName);
                String rawArgs = matcher.group(4).trim();
                System.out.println("method ::: " + method.getName());

//                    System.out.println("Метод: " + methodName);
//                    System.out.println("parent after::: " + curEntity.getParent().getPath());
//                if (rawArgs.isEmpty()) {
////                        System.out.println("  Аргументы: [нет]");
//                    method.setChildren(new ArrayList<>());
//                } else {
//                    // Сплитим аргументы по запятой, игнорируя запятые внутри < >
//                    String[] argsArray = rawArgs.split(",\\s*(?![^<]*>)");
////                        System.out.println("  Количество аргументов: " + argsArray.length);
//
//                    for (int j = 0; j < argsArray.length; ++j) {
//                        String arg = argsArray[j].trim();
//                        // Разделяем тип и имя (последнее слово - имя, всё до него - тип)
//                        int lastSpace = arg.lastIndexOf(" ");
//                        String type = arg.substring(0, lastSpace).trim();
//                        String name = arg.substring(lastSpace).trim();
//
////                            System.out.println("    " + (j + 1) + ". Тип: [" + type + "], Имя: [" + name + "]");
//                        StructureEntity argument = new StructureEntity("ARGUMENT", method.getPath(), method.getLevel() + 1);
//                        argument.setName(name);
//                        argument.setParent(method);
//                        method.addChild(argument);
//                    }
////                        System.out.println("parent after::: " + curEntity.getParent().getPath());
//                }
//                if (Objects.equals(curEntity.getName(), "GameConfiguration.java"))
//                    System.out.println("class ::: " + curEntity.getName());
                method.setParent(curEntity);
                System.out.println("method parent::: " + method.getParent().getName());
//                    method.print();
//                    System.out.println("parent after::: " + curEntity.getParent().getPath());
                curEntity.addChild(method);
                System.out.println("parent children ::: " + curEntity.getChildren().size());
//                    curEntity.print();
//                    System.out.println("-----------------------------------");
//                    System.out.println("parent after::: " + curEntity.getParent().getPath());
            }
        }
//        curEntity.print();
//        System.out.println("---------------------------------------------");
        return curEntity;
    }

//    private StructureEntity findClasses() {
//        return
//    }

//    private void fileProcessing(Path sourceFile) throws IOException {
//        // Читаем все содержимое файла
//        System.out.println(sourceFile);
//        String content = Files.readString(sourceFile);
//
//        String processedContent = content;
//        Files.writeString(sourceFile , processedContent);
//    }


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


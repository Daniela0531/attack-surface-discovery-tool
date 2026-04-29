package org.example.structure;

import io.joern.javasrc2cpg.JavaSrc2Cpg;
import io.joern.javasrc2cpg.Config;
import io.shiftleft.codepropertygraph.generated.Cpg;
import io.shiftleft.codepropertygraph.generated.NodeTypes;
import io.shiftleft.codepropertygraph.generated.nodes.Call;
import io.shiftleft.codepropertygraph.generated.nodes.Method;
import io.shiftleft.codepropertygraph.generated.nodes.TypeDecl;
import scala.jdk.javaapi.CollectionConverters;

import java.util.Iterator;


public class StructureBuilderJoern {
    public static void main(String[] args) {
        // 1. Загрузить CPG. Конкретный Java-метод нужно найти в API.
        Cpg cpg = Cpg.importFromFile("путь/к/вашему/cpg.bin");

        // 2. Запросить и вывести все классы
        System.out.println("--- Классы ---");
        cpg.typeDecl().forEach(typeDecl ->
                System.out.println(typeDecl.fullName()) // Выводим полное имя класса
        );

        // 3. Запросить и вывести все методы
        System.out.println("--- Методы ---");
        cpg.method().forEach(method ->
                System.out.println(method.name()) // Выводим имя метода
        );
//        // Настройка
//        Config config = new Config().withInputPath("путь/к/коду");
//
//        // Создание CPG
//        Cpg cpg = new JavaSrc2Cpg().createCpg(config);
//
//        // Обход узлов через стандартный итератор OverflowDB
//        Iterator<overflowdb.Node> typeDecls = cpg.graph().nodes(NodeTypes.TYPE_DECL);
//
//        while (typeDecls.hasNext()) {
//            TypeDecl type = (TypeDecl) typeDecls.next();
//
//            // Фильтруем внешние типы (библиотеки)
//            if (type.isExternal()) continue;
//
//            System.out.println("Class: " + type.fullName());
//
//            // Используем конвертер для Scala Iterator -> Java Iterable
//            CollectionConverters.asJava(type.astOut()).forEach(astNode -> {
//                if (astNode instanceof Method) {
//                    Method m = (Method) astNode;
//                    System.out.println("  Method: " + m.name() + " " + m.signature());
//
//                    // Переходы к вызовам
//                    CollectionConverters.asJava(m.containsOut()).forEach(content -> {
//                        if (content instanceof Call) {
//                            Call call = (Call) content;
//                            System.out.println("    -> " + call.methodFullName());
//                        }
//                    });
//                }
//            });
//        }
//
//        cpg.close(); // Важно закрыть граф
    }

}

package org.example.structure;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;

import java.io.File;

public class StructureBuilderJavaParser {
    public void createStructure(String fileName) throws Exception {
        // 1. Парсим файл
        CompilationUnit cu = StaticJavaParser.parse(new File("src/main/java/MyClass.java"));

        // 2. Находим все декларации методов (MethodDeclaration)
        cu.findAll(MethodDeclaration.class).forEach(method -> {
            System.out.println("Найден метод: " + method.getName());
            System.out.println("Тип возвращаемого значения: " + method.getType());
        });
    }

    // Посетитель, который ищет все целые числа в коде
    class IntegerVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(IntegerLiteralExpr n, Void arg) {
            super.visit(n, arg);
            System.out.println("Найдено число в коде: " + n.getValue());
        }
    }

// Запуск:
// cu.accept(new IntegerVisitor(), null);
}

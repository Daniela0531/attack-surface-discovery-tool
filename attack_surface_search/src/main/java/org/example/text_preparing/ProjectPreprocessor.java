package org.example.text_preparing;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ProjectPreprocessor {
    private Path source;
    public ProjectPreprocessor(Path source) {
        this.source = source;
    }
    public void processProject() throws IOException {
//        System.out.println("start");

        try (Stream<Path> stream = Files.walk(source)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(file -> {
                        try {
//                            System.out.println(file.toString());
                            preprocessAndSave(file);
                        } catch (IOException e) {
                            System.err.println("Ошибка при обработке файла: " + file);
                        }
                    });
        }
    }

    // нужно добавить обработку лишних энтеров:
    // посплитить всё по ;
    // далее посплитить по всем пробельным символам (пробел , перенос строки , прочее)
    // заменить (склеить) всё по пробелам
    // далее решить:
    // точки , запятые , точка с запитой , символы равно , минус , плюс , процент , логические операции - обрамляем пробелами
    private void preprocessAndSave(Path sourceFile) throws IOException {
        // Читаем все содержимое файла
        String content = Files.readString(sourceFile);

        String processedContent = content;
        processedContent = deleteComments(processedContent);
        processedContent = preprocessSpecialSimbol(processedContent);
        processedContent = preprocessOperation(processedContent);
//        processedContent = preprocessMethods(processedContent);
//        processedContent = normalizeSpaces(processedContent);
        Files.writeString(sourceFile , processedContent);
    }

//    private String preprocessMethods(String content) {
//        String before = "(|public|private|protected|static|\\s+\\w+[\\w\\<\\>\\[\\]\\s]+\\w+\\s*\\([^)]*\\)\\s*[;\\{]?)";
//        String after = "$1\n";
//        return content.replaceAll(before , after);
//    }

    private String preprocessSpecialSimbol(String content) {
        String processedContent = content.replaceAll("," , " , ");
        processedContent = processedContent.replaceAll("\\." , " . ");
        processedContent = processedContent.replaceAll(";" , " ;\n");
        processedContent = processedContent.replaceAll("\\(" , " \\( ");
        processedContent = processedContent.replaceAll("\\)" , " \\) ");
        processedContent = processedContent.replaceAll("\\{" , " \\{ ");
        processedContent = processedContent.replaceAll("\\}" , " \\} ");
        return processedContent;
    }

    private String preprocessOperation(String content) {
        String processedContent = replaceAllWithoutStrings(content, "\\+\\+" , " \\+\\+ ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\+\\=", " += ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<!\\+)\\+(?!\\+)", " + ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\-\\-" , " -- ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\-\\=", " -= ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<!\\-)\\-(?!\\-)", " - ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\=\\=" , " == ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<!\\>)\\>\\=" , " >= ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<!\\<)\\<\\=" , " <= ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\<\\<\\<" , " <<< ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\>\\>\\>" , " >>> ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<!\\>)\\>\\>(?!\\>)", " >> ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\>\\>\\=", " >>= ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<!\\<)\\<\\<(?!\\<)", " << ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\<\\<\\=", " <<= ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<![>=])\\>(?![>=])", " > ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<!\\<)\\<(?!\\<)", " < ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\?" , " ? ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\|\\|" , " || ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\|\\=", " |= ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<![|=])\\|(?![|=])", " | ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\&\\&" , " && ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\&\\=", " &= ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<!\\&)\\&(?!\\&)", " & ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\%\\=", " %= ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<![=])\\%(?![=])" , " % ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\//", " // ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\/\\=", " /= ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<![/=])\\/(?![/=])", " / ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\*\\*", " ** ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\*\\=", " *= ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<![*=])\\*(?![*=])", " * ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\$" , " $ ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\@" , " @ ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\^\\=", " ^= ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\^" , " ^ ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\!\\=", " != ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\!" , " ! ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\~", " ~ ");
        processedContent = replaceAllWithoutStrings(processedContent,"\\:\\:", " :: ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<!\\:)\\:(?!\\:)", " : ");
        processedContent = replaceAllWithoutStrings(processedContent,"(?<![+-/*|<>!=%&^])=(?![+-/*|<>!=%&^])", " = ");
//        String processedContent = content.replaceAll("\\+\\+" , " \\+\\+ ");
//        processedContent = processedContent.replaceAll("\\+\\=", " += ");
//        processedContent = processedContent.replaceAll("(?<!\\+)\\+(?!\\+)", " + ");
//        processedContent = processedContent.replaceAll("\\-\\-" , " -- ");
//        processedContent = processedContent.replaceAll("\\-\\=", " -= ");
//        processedContent = processedContent.replaceAll("(?<!\\-)\\-(?!\\-)", " - ");
//        processedContent = processedContent.replaceAll("\\=\\=" , " == ");
//        processedContent = processedContent.replaceAll("(?<!\\>)\\>\\=" , " >= ");
//        processedContent = processedContent.replaceAll("(?<!\\<)\\<\\=" , " <= ");
//        processedContent = processedContent.replaceAll("\\<\\<\\<" , " <<< ");
//        processedContent = processedContent.replaceAll("\\>\\>\\>" , " >>> ");
//        processedContent = processedContent.replaceAll("(?<!\\>)\\>\\>(?!\\>)", " >> ");
//        processedContent = processedContent.replaceAll("\\>\\>\\=", " >>= ");
//        processedContent = processedContent.replaceAll("(?<!\\<)\\<\\<(?!\\<)", " << ");
//        processedContent = processedContent.replaceAll("\\<\\<\\=", " <<= ");
//        processedContent = processedContent.replaceAll("(?<![>=])\\>(?![>=])", " > ");
//        processedContent = processedContent.replaceAll("(?<!\\<)\\<(?!\\<)", " < ");
//        processedContent = processedContent.replaceAll("\\?" , " ? ");
//        processedContent = processedContent.replaceAll("\\|\\|" , " || ");
//        processedContent = processedContent.replaceAll("\\|\\=", " |= ");
//        processedContent = processedContent.replaceAll("(?<![|=])\\|(?![|=])", " | ");
//        processedContent = processedContent.replaceAll("\\&\\&" , " && ");
//        processedContent = processedContent.replaceAll("\\&\\=", " &= ");
//        processedContent = processedContent.replaceAll("(?<!\\&)\\&(?!\\&)", " & ");
//        processedContent = processedContent.replaceAll("\\%\\=", " %= ");
//        processedContent = processedContent.replaceAll("(?<![=])\\%(?![=])" , " % ");
//        processedContent = processedContent.replaceAll("\\//", " // ");
//        processedContent = processedContent.replaceAll("\\/\\=", " /= ");
//        processedContent = processedContent.replaceAll("(?<![/=])\\/(?![/=])", " / ");
//        processedContent = processedContent.replaceAll("\\*\\*", " ** ");
//        processedContent = processedContent.replaceAll("\\*\\=", " *= ");
//        processedContent = processedContent.replaceAll("(?<![*=])\\*(?![*=])", " * ");
//        processedContent = processedContent.replaceAll("\\$" , " $ ");
//        processedContent = processedContent.replaceAll("\\@" , " @ ");
//        processedContent = processedContent.replaceAll("\\^\\=", " ^= ");
//        processedContent = processedContent.replaceAll("\\^" , " ^ ");
//        processedContent = processedContent.replaceAll("\\!\\=", " != ");
//        processedContent = processedContent.replaceAll("\\!" , " ! ");
//        processedContent = processedContent.replaceAll("\\~", " ~ ");
//        processedContent = processedContent.replaceAll("\\:\\:", " :: ");
//        processedContent = processedContent.replaceAll("(?<!\\:)\\:(?!\\:)", " : ");
//        processedContent = processedContent.replaceAll("(?<![+-/*|<>!=%&^])=(?![+-/*|<>!=%&^])", " = ");
        return processedContent;
    }

    private String normalizeSpaces(String content) {
        String processedContent = replaceAllWithoutStrings(content,"[\\s\\t]+" , " ");
        processedContent = replaceAllWithoutStrings(processedContent,"[\\s\\t]*\\{[\\s\\t]*" , "\n\\{\n");
        processedContent = replaceAllWithoutStrings(processedContent,"[\\s\\t]*\\}[\\s\\t]*" , "\n\\}\n");
        processedContent = replaceAllWithoutStrings(processedContent,"[\n]+" , "\n");
        return processedContent;
    }

    private String deleteComments(String content) {
        // \\\\ в регулярке даёт один \
        String regex = "(\"(?:[^\"\\\\]|\\\\.)*\")|((?s)/\\*.*?\\*/)|(?:\\s*//.*)+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Если нашлась Группа 1 (строка в кавычках),
                // вставляем её обратно без изменений
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group(1)));
            } else {
                // Если нашлись комментарии (Группы 2 или 3),
                // заменяем их на пустую строку
                matcher.appendReplacement(sb, "");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
//        String processedContent = content.replaceAll("(\"(?:[^\"\\\\]|\\\\.)*\")|((?s)/\\*.*?\\*/)", "");
//        System.out.println(processedContent);
//        processedContent = processedContent.replaceAll("(\"(?:[^\"\\\\]|\\\\.)*\")|(?:\\s*//.*)+" , "");
//        return processedContent;
    }

    private String replaceAllWithoutStrings(String content, String regex_, String replacement) {
        String regex = "(\"(?:[^\"\\\\]|\\\\.)*\")|".concat(regex_);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Если нашлась Группа 1 (строка в кавычках),
                // вставляем её обратно без изменений
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group(1)));
            } else {
                // Если нашлись комментарии (Группы 2 или 3),
                // заменяем их на пустую строку
                matcher.appendReplacement(sb, replacement);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

// +
// -
// *
// /
// %
// ++
// --
// 2. Логические операции
// &&
// ||
// !
// ^
// 3. Операции сравнения
// ==
// !=
// >
// <
// >=
// <=
// 4. Побитовые операции (работают с битами чисел)
// & — побитовое И.
// | — побитовое ИЛИ.
// ~ — побитовое НЕ (инверсия).
// ^ — побитовое исключающее ИЛИ.
// << — сдвиг влево.
// >> — сдвиг вправо (с сохранением знака).
// >>> — беззнаковый сдвиг вправо.
// 5. Сокращенные присваивания
// Комбинации знака операции и =: +=, -=, *=, /=, %=, &=, ^=, |=, <<=, >>=, >>>=.
// 6. Тернарный оператор
// ? :
}

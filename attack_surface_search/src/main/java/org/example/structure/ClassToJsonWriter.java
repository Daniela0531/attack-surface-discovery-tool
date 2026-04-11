package org.example.structure;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ClassToJsonWriter {
    private Path target;
    private ObjectMapper mapper = new ObjectMapper();
    public ClassToJsonWriter(Path target) {
        this.target = target;
        this.mapper = new ObjectMapper();
    }

    public void writeToJson(StructureEntity curEntity) throws Exception {
        writeToJson(curEntity, 0, true);
    }

    private void writeToJson(StructureEntity curEntity, int extraTabs, Boolean isLast) throws Exception {
        String startTabs = "  ".repeat(curEntity.getLevel() + extraTabs);
        String bodyTabs = "  ".repeat(curEntity.getLevel() + 1 + extraTabs);
        String contentTabs = "  ".repeat(curEntity.getLevel() + 2 + extraTabs);
        String type = curEntity.getType();
        String name = curEntity.getName();
        ArrayList<String> content = curEntity.getContent();

        String jsonString = startTabs + "{\n" +
                bodyTabs + "\"type\": \"" + type + "\",\n" +
                bodyTabs + "\"name\": \"" + name + "\",\n";
//        if (!content.isEmpty()) {
//            jsonString += bodyTabs + "\"content\": [\n";
//            for (int i = 0; i < content.size(); ++i) {
//                String str = String.join("\\\"", content.get(i).split("\""));
//                if (i < content.size() - 1) {
//                    jsonString += contentTabs + "\"" + str + "\",\n";
//                } else {
//                    jsonString += contentTabs + "\"" + str + "\"\n";
//                }
//            }
//            jsonString += bodyTabs + "],\n";
//        }
        jsonString += bodyTabs + "\"children\": [";
        if (curEntity.getChildren().isEmpty()) {
            jsonString += "]\n" + startTabs + "}";
            if (!isLast) {
                jsonString += ",\n";
            } else {
                jsonString += "\n";
            }
            Files.write(
                    target,
                    jsonString.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            return;
        }
        extraTabs += 1;
        jsonString += "\n";
        Files.write(
                target,
                jsonString.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        ArrayList<StructureEntity> children = curEntity.getChildren();
        boolean childIsLast = false;
        for (int i = 0; i < children.size(); ++i) {
            if (i == children.size() - 1) {
                childIsLast = true;
            }
            writeToJson(children.get(i), extraTabs, childIsLast);
        }
        String finishString = bodyTabs + "]\n" + startTabs + "}";
        if (!isLast) {
            finishString += ",\n";
        } else {
            finishString += "\n";
        }
        Files.write(
                target,
                finishString.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);

    }
}

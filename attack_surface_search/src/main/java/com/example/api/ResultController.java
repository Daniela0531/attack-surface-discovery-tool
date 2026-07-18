package com.example.api;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ResultController {

    private final String FILES_DIR = "./results"; // путь к папке с result1.json, result2.json, ...
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<Map<String, Object>> getResults(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) throws IOException {

        // Получаем все файлы result*.json
        Path dir = Paths.get(FILES_DIR);
        List<Path> files = Files.list(dir)
                .filter(path -> path.getFileName().toString().matches("result\\d+\\.json"))
                .sorted()
                .toList();

        int totalElements = files.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        // Вычисляем границы для пагинации
        int start = pageNum * pageSize;
        int end = Math.min(start + pageSize, totalElements);

        // Собираем содержимое нужных файлов
        List<Object> content = new ArrayList<>();
        for (int i = start; i < end; i++) {
            String json = Files.readString(files.get(i));
            content.add(objectMapper.readValue(json, Object.class));
        }

        // Формируем ответ
        Map<String, Object> response = new HashMap<>();
        response.put("content", content);
        response.put("pageNum", pageNum);
        response.put("pageSize", pageSize);
        response.put("totalElements", totalElements);
        response.put("totalPages", totalPages);
        response.put("last", pageNum >= totalPages - 1);

        return ResponseEntity.ok(response);
    }
}

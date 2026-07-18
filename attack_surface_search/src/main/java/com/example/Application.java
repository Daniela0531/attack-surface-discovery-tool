package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {


        // 2. Запускаем Spring Boot (контроллер)
        SpringApplication.run(Application.class, args);
        // 1. Запускаем ваш Main класс
        Main.main(args);
    }
}
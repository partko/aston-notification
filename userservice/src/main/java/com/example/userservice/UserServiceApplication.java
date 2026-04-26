package com.example.userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);

    public static void main(String[] args) {
        logger.info("Запуск приложения");

        try {
            SpringApplication.run(UserServiceApplication.class, args);
            logger.info("Приложение успешно запущено");
        } catch (Exception e) {
            logger.error("Ошибка при запуске или выполнении приложения", e);
            throw e;
        }
    }
}

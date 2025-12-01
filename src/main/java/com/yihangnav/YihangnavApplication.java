package com.yihangnav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableScheduling
public class YihangnavApplication {

    static {
        try {
            Files.createDirectories(Paths.get("./data"));
        } catch (Exception ignored) {
            // ensure SQLite parent dir exists
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(YihangnavApplication.class, args);
    }

}

package com.yihangnav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YihangnavApplication {

    public static void main(String[] args) {
        SpringApplication.run(YihangnavApplication.class, args);
    }

}

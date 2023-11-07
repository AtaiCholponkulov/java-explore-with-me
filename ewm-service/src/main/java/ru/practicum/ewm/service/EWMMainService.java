package ru.practicum.ewm.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.practicum.stats.client.model", "ru.practicum.ewm.service"})
public class EWMMainService {

    public static void main(String[] args) {
        SpringApplication.run(EWMMainService.class, args);
    }
}

package com.example.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        /*SpringApplication application = new SpringApplication(Application.class);
        application.setAdditionalProfiles("ssl");
        application.run(args);*/
    }
}

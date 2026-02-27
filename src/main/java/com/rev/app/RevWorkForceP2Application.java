package com.rev.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.rev.app")
public class RevWorkForceP2Application {

    public static void main(String[] args) {
        SpringApplication.run(RevWorkForceP2Application.class, args);
    }
}
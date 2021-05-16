package com.epam.esm.web;

import com.epam.esm.persistence.config.PersistenceConfig;
import com.epam.esm.service.config.ServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.epam.esm.web")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(new Class<?>[]{Application.class, ServiceConfiguration.class, PersistenceConfig.class}, args);
    }
}

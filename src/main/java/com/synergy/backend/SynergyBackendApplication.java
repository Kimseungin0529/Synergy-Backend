package com.synergy.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SynergyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynergyBackendApplication.class, args);
    }

}

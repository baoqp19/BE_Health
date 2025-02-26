package com.example.HealthCare.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties("spring.datasource")
@Profile("dev")
public class DBDevConfiguration {
    @PostConstruct
    public void devDatabaseConnection() {
        System.out.println("Current Profile: Dev");
    }
}

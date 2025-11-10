package com.tutomato.delivery;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestContainerConfiguration {

    public static final PostgreSQLContainer<?> postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("delivery")
            .withUsername("delivery")
            .withPassword("delivery")
            .withEnv("TZ", "UTC");

        postgresContainer.start();

        System.setProperty(
            "spring.datasource.url",
            postgresContainer.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=Asia/Seoul"
        );
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @PreDestroy
    public void preDestroy() {
        if (postgresContainer.isRunning()) {
            postgresContainer.stop();
        }
    }

}

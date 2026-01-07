package com.foodkeeper.foodkeeperserver.support.integration;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.mysql.MySQLContainer;

@IntegrationTest
@ActiveProfiles("test")
public class SpringTest {

    static MySQLContainer container = new MySQLContainer("mysql:8.0.44-debian")
            .withDatabaseName("foodkeeper")
            .withUsername("test")
            .withPassword("test");

    static {
        container.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}

package com.foodkeeper.foodkeeperserver.support.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.mysql.MySQLContainer;

@ActiveProfiles("test")
@DataJpaTest
public class RepositoryTest {

    static MySQLContainer container = new MySQLContainer("mysql:8.0.44-debian")
            .withDatabaseName("foodkeeper")
            .withUsername("test")
            .withPassword("test");
    @Autowired protected TestEntityManager em;

    static {
        container.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeEach
    void initData() {
        em.flush();
        em.clear();
    }
}

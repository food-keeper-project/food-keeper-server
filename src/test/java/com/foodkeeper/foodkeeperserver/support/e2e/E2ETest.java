package com.foodkeeper.foodkeeperserver.support.e2e;

import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.mysql.MySQLContainer;

@IntegrationTest
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ETest {

    static MySQLContainer container = new MySQLContainer("mysql:8.0.44-debian")
            .withDatabaseName("foodkeeper")
            .withUsername("test")
            .withPassword("test");
    protected static final String AUTHORIZATION = "Authorization";

    @LocalServerPort int port;
    @Autowired JwtGenerator jwtGenerator;

    protected RestTestClient client;

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
    void initClient() {
        client = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    protected String getAccessToken(String memberKey) {
        return "Bearer " + jwtGenerator.generateJwt(memberKey).accessToken();
    }
}

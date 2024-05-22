package com.dawidrozewski.sandbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class AbstractConfiguredTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Container
    public static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.8")
            .withDatabaseName("integration-tests-db")
            .withUsername("username")
            .withPassword("password");

    static {
        mariaDBContainer.start();
    }

    @DynamicPropertySource
    static void mariaDBProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);
    }
}

package com.example.demo;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Container
    public GenericContainer<?> devapp = new GenericContainer<>("app-dev:0.0.1")
            .withExposedPorts(8080);

    @Container
    public GenericContainer<?> prodapp = new GenericContainer<>("app-prod:0.0.1")
            .withExposedPorts(8081);

    @BeforeEach
    public void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void contextLoads() {
        ResponseEntity<String> forEntityDev = restTemplate.getForEntity("http://localhost:" + devapp.getMappedPort(8080) + "/profile", String.class);
        System.out.println(forEntityDev.getBody());
        Assert.assertEquals(forEntityDev.getBody(), "Current profile is dev");

        ResponseEntity<String> forEntityProd = restTemplate.getForEntity("http://localhost:" + prodapp.getMappedPort(8081) + "/profile", String.class);
        System.out.println(forEntityProd.getBody());
        Assert.assertEquals(forEntityProd.getBody(), "Current profile is production");
    }
}


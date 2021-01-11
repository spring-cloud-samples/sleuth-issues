package com.thagul.testcbr4j;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = {Testcbr4jApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class Testcbr4jApplicationTests {

    @LocalServerPort
    private Integer localServerPort;

    @Test
    public void shouldUseCircuitBreakerFeignBuilder() {
        String response = new RestTemplate().getForObject("http://localhost:" + localServerPort + "/main/test", String.class);

        BDDAssertions.then(response).isEqualTo("{\"id\":\"fallback\"}");
    }

}


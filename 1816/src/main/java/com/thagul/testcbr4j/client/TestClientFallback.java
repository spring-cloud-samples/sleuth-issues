package com.thagul.testcbr4j.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import java.util.Map;

@Slf4j
@Component
public class TestClientFallback implements FallbackFactory<TestClient> {
    @Override
    public TestClient create(Throwable cause) {
        return new TestClient() {
            @Override
            public Map<String, Object> find(Integer id) {
                log.error("Feign client fallback: " + cause.getMessage());
                return Map.of("id", "fallback");
            }
        };
    }
}

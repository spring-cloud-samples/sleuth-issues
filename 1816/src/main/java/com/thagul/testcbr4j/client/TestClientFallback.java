package com.thagul.testcbr4j.client;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestClientFallback implements FallbackFactory<TestClient> {
	@Override
	public TestClient create(Throwable cause) {
		return id -> {
			log.error("Feign client fallback: " + cause.getMessage());
			Map<String, Object> map = new HashMap<>();
			map.put("id", "fallback");
			return map;
		};
	}
}

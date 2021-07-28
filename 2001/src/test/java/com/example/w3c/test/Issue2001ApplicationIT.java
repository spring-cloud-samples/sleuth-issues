package com.example.w3c.test;

import java.net.URI;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.sleuth.propagation.type=W3C")
class Issue2001ApplicationIT {

	@LocalServerPort
	int port;

	@Test
	public void shouldReadW3cTraceStateAndBaggage() {
		String traceParentHeader = "00-8df82d9e560f4c318f2c3c1ce7d69a0a-8dc0d640e5c62ebe-01";
		String baggageHeader = "mybaggage=mybaggagevalue";
		String tracestate = "sappp=CwAAmEnGj0gThK52TCXZ270X8nBhc3Nwb3J0LWFwcABQT1NU";
		String myBaggage = "mybaggagevalue";
		String response = new RestTemplate().exchange(RequestEntity.get(URI.create("http://localhost:" + port + "/"))
				.header("traceparent", traceParentHeader)
				.header("tracestate", tracestate)
				.header("baggage", baggageHeader)
				.build(), String.class).getBody();

		BDDAssertions.then(response).isEqualTo("Headers tracestate: [" + tracestate + "] baggage: [" + baggageHeader + "]. Baggage tracestate: [" + tracestate + "] mybaggage [" + myBaggage + "]");
	}

	@Configuration(proxyBeanMethods = false)
	@EnableAutoConfiguration
	static class Config {

		@Bean
		TestWebController testWebController(RestTemplate restTemplate, Environment environment) {
			return new TestWebController(restTemplate, environment.getProperty("test.server.port", Integer.class, 4321));
		}

		@Bean
		RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

}

@RestController
class TestWebController {

	private final RestTemplate restTemplate;

	private final int port;

	TestWebController(RestTemplate restTemplate, int port) {
		this.restTemplate = restTemplate;
		this.port = port;
	}

	@GetMapping("/")
	public String preference(@RequestHeader("tracestate") String tracestateHeader, @RequestHeader("baggage") String baggageHeader) {
		return this.restTemplate.getForObject("http://localhost:" + this.port, String.class);
	}
}


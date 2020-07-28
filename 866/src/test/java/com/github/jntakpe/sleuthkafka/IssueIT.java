package com.github.jntakpe.sleuthkafka;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import brave.Span;
import brave.Tracer;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = IssueIT.Config.class)
@ActiveProfiles("test")
public class IssueIT {

	@Value("${test.server.port:7654}") int port;
	@Autowired RestTemplate restTemplate;
	@Autowired Tracer tracer;

	@Test
	public void should_propagate_trace_id() throws IOException {
		//given
		Span span = this.tracer.nextSpan().name("foo").start();
		String traceIdString = span.context().traceIdString();
		try (Tracer.SpanInScope ws = this.tracer.withSpanInScope(span)) {
			//when
			this.restTemplate
					.getForObject("http://localhost:" + this.port + "/kafka/template", String.class);
			this.restTemplate
					.getForObject("http://localhost:" + this.port + "/kafka/reactor", String.class);
		} finally {
			span.finish();
		}

		//then
		String text = new String(Files.readAllBytes(new File("target/log/spring.log").toPath()));
		List<String> traceIds = Arrays.stream(text.split("\n"))
				.filter(s -> s.contains("[TEST]")).map(s -> s.split(",")[1]).distinct()
				.collect(Collectors.toList());
		BDDAssertions
				.then(traceIds).hasSize(1)
				.containsOnly(traceIdString);
	}

	@Configuration
	@EnableAutoConfiguration
	static class Config {
		@Bean
		RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

}

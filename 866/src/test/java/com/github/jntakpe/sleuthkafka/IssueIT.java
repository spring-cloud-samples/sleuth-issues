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
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = {SleuthKafkaApplication.class, IssueIT.Config.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ExtendWith(OutputCaptureExtension.class)
public class IssueIT {

	@LocalServerPort
	int port;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	Tracer tracer;

	@Container
	static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"));

	@DynamicPropertySource
	static void kafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@Test
	public void should_propagate_trace_id(CapturedOutput capturedOutput) throws IOException {
		//given
		Span span = this.tracer.nextSpan().name("foo").start();
		String traceIdString = span.context().traceIdString();
		try (Tracer.SpanInScope ws = this.tracer.withSpanInScope(span)) {
			//when
			this.restTemplate
					.getForObject("http://localhost:" + this.port + "/kafka/template", String.class);
			this.restTemplate
					.getForObject("http://localhost:" + this.port + "/kafka/reactor", String.class);
		}
		finally {
			span.finish();
		}

		//then
		String text = capturedOutput.getAll();
		List<String> traceIds = Arrays.stream(text.split("\n"))
				.filter(s -> s.contains("[TEST]")).map(s -> s.split(",")[1]).distinct()
				.collect(Collectors.toList());
		BDDAssertions
				.then(traceIds).hasSize(1)
				.containsOnly(traceIdString);
	}

	@Configuration
	static class Config {
		@Bean
		RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

}

package com.github.goober.sleuthbug;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import brave.Span;
import brave.Tracer;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IssueIT.Config.class)
@ActiveProfiles("test")
public class IssueIT {

	@Value("${test.server.port:7655}") int port;
	@Autowired RestTemplate restTemplate;
	@Autowired Tracer tracer;

	@Test
	public void should_propagate_trace_id() throws IOException, InterruptedException {
		//given
		Span span = this.tracer.nextSpan().name("foo").start();
		try (Tracer.SpanInScope ws = this.tracer.withSpanInScope(span)) {
			//when
			this.restTemplate
					.getForObject("http://localhost:" + this.port + "/users", String.class);
		} finally {
			span.finish();
		}

		//then
		String text = new String(Files.readAllBytes(new File("target/log/spring.log").toPath()));
		List<String> callIds = Arrays.stream(text.split("\n"))
				.filter(s -> s.contains("Handled send of NoopSpan(")).map(s -> s.split("/")[1].trim().substring(0, 16))
				.collect(Collectors.toList());
		System.out.println("Found the following call ids " + callIds);
		List<String> parentSpanIds = Arrays.stream(text.split("\n"))
				.filter(s -> callIds.stream()
					.anyMatch(callId -> s.contains("," + callId)))
				.map(s -> s.split(",")[1].trim())
				.collect(Collectors.toList());
		System.out.println("Found the following parent span ids " + parentSpanIds);
		List<String> strings = parentSpanIds.stream().distinct().collect(Collectors.toList());
		// they should all share a single Parent Span Id
		BDDAssertions
				.then(strings)
				.hasSize(1);
		BDDAssertions.then(strings.get(0)).isNotBlank();
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

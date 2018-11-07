package com.github.goober.sleuthbug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
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
	public void should_propagate_trace_id() throws IOException {
		//given
		Span span = tracer.nextSpan().name("foo").start();
		try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
			//when
			restTemplate
					.getForObject("http://localhost:" + port + "/users", String.class);
		} finally {
			span.finish();
		}

		//then
		String text = new String(Files.readAllBytes(new File("target/log.log").toPath()));
		List<String> traceIds = Arrays.stream(text.split("\n"))
				.filter(s -> s.contains("X-B3-ParentSpanId")).map(s -> s.split(":")[1].trim())
				.collect(Collectors.toList());
		BDDAssertions
				// 4 calls should have X-B3-ParentSpanId
				.then(traceIds)
				.hasSize(4);
		List<String> strings = traceIds.stream().distinct().collect(Collectors.toList());
		// with Greenwich we expect each call to have a different parent
		BDDAssertions
				.then(strings)
				.hasSize(3);
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

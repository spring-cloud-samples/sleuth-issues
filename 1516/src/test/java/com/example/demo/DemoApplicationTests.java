package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.assertj.core.api.BDDAssertions;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;
import zipkin2.Span;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.sleuth.util.ArrayListSpanReporter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void should_set_same_parent_span_id_of_webclient_spans_when_app_is_non_webflux() {
		ConfigurableApplicationContext service2 = null;
		ConfigurableApplicationContext service1 = null;
		try {
			service2 = service("service2");
			int service2Port = port(service2);
			service1 = service("service1", service2Port);
			int service1Port = port(service1);

			String response = new RestTemplate().getForObject("http://localhost:" + service1Port + "/call", String.class);
			BDDAssertions.then(response).isEqualTo("123");

			List<Span> spans = allReportedSpans(service2, service1);
			thenHasOnlyOneTraceId(spans);
			String firstSpanIdInController = firstSpan(service1).context().spanIdString();
			thenAllPostWebClientSpansAreEqualToFirstSpanInTheController(service1, firstSpanIdInController);
			thenAllWebClientSpansHaveSameParentId(spans, firstSpanIdInController);
		}
		finally {
			if (service2 != null) {
				service2.close();
			}
			if (service1 != null) {
				service1.close();
			}
		}

	}

	private ListAssert<String> thenAllPostWebClientSpansAreEqualToFirstSpanInTheController(ConfigurableApplicationContext service1, String firstSpanIdInController) {
		return BDDAssertions.then(postWebClientSpans(service1).stream().map(span -> span.context().spanIdString()).distinct().collect(Collectors.toList())).containsExactly(firstSpanIdInController);
	}

	private List<Span> allReportedSpans(ConfigurableApplicationContext service2, ConfigurableApplicationContext service1) {
		ArrayListSpanReporter service1Reporter = reporter(service1);
		ArrayListSpanReporter service2Reporter = reporter(service2);
		List<Span> spans = new ArrayList<>(service1Reporter.getSpans());
		spans.addAll(service2Reporter.getSpans());
		return spans;
	}

	private void thenHasOnlyOneTraceId(List<Span> spans) {
		BDDAssertions.then(spans.stream().map(Span::traceId).collect(Collectors.toSet())).hasSize(1);
	}

	private void thenAllWebClientSpansHaveSameParentId(List<Span> spans, String firstSpanIdInController) {
		BDDAssertions.then(spans.stream().filter(span -> "CLIENT".equals(span.kind().name()))
				.map(Span::parentId).collect(Collectors.toSet())).hasSize(1).containsExactly(firstSpanIdInController);
	}

	private ConfigurableApplicationContext service(String name) {
		return service(name, 9999);
	}

	private ConfigurableApplicationContext service(String name, int otherServicePort) {
		return new SpringApplicationBuilder(
				DemoApplication.class)
				.web(WebApplicationType.REACTIVE)
				.properties("server.port=0", "spring.jmx.enabled=false",
						"spring.application.name=" + name,
						"security.basic.enabled=false",
						"management.security.enabled=false",
						"service.port=" + otherServicePort)
				.run();
	}

	int port(ApplicationContext context) {
		return context.getBean(Environment.class).getProperty("local.server.port",
				Integer.class);
	}

	ArrayListSpanReporter reporter(ApplicationContext context) {
		return context.getBean(ArrayListSpanReporter.class);
	}

	brave.Span firstSpan(ApplicationContext context) {
		return context.getBean(DemoController.class).firstSpan;
	}

	Queue<brave.Span> postWebClientSpans(ApplicationContext context) {
		return context.getBean(DemoController.class).postWebClientSpans;
	}
}

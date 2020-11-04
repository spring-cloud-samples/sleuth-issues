package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import brave.handler.MutableSpan;
import org.assertj.core.api.BDDAssertions;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
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

			List<MutableSpan> spans = allReportedSpans(service2, service1);
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
		return BDDAssertions.then(postWebClientSpans(service1).stream().map(span -> span.context().spanIdString()).distinct().collect(Collectors.toList()))
				.containsExactly(firstSpanIdInController);
	}

	private List<MutableSpan> allReportedSpans(ConfigurableApplicationContext service2, ConfigurableApplicationContext service1) {
		TestSpanHandler service1Reporter = handler(service1);
		TestSpanHandler service2Reporter = handler(service2);
		List<MutableSpan> spans = new ArrayList<>(service1Reporter.spans());
		spans.addAll(service2Reporter.spans());
		return spans;
	}

	private void thenHasOnlyOneTraceId(List<MutableSpan> spans) {
		BDDAssertions.then(spans.stream().map(MutableSpan::traceId).collect(Collectors.toSet())).hasSize(1);
	}

	private void thenAllWebClientSpansHaveSameParentId(List<MutableSpan> spans, String firstSpanIdInController) {
		BDDAssertions.then(spans.stream().filter(span -> "CLIENT".equals(span.kind().name()))
				.map(MutableSpan::parentId).collect(Collectors.toSet())).hasSize(1).containsExactly(firstSpanIdInController);
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

	TestSpanHandler handler(ApplicationContext context) {
		return context.getBean(TestSpanHandler.class);
	}

	brave.Span firstSpan(ApplicationContext context) {
		return context.getBean(DemoController.class).firstSpan;
	}

	Queue<brave.Span> postWebClientSpans(ApplicationContext context) {
		return context.getBean(DemoController.class).postWebClientSpans;
	}
}

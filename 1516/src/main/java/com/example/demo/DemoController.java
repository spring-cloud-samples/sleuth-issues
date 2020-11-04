package com.example.demo;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import brave.Span;
import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class DemoController {

	private static final Logger log = LoggerFactory.getLogger(DemoController.class);

	private final WebClient webClient;

	private final Tracer tracer;

	Queue<Span> postWebClientSpans = new LinkedBlockingQueue<>();

	Span firstSpan;

	@Autowired
	public DemoController(final WebClient.Builder webClientBuilder, Tracer tracer, @Value("${service.port:9999}") int port) {
		this.webClient = webClientBuilder
				.baseUrl("http://localhost:" + port)
				.build();
		this.tracer = tracer;
	}

	@GetMapping("/1")
	public String endpoint1() {
		return "1";
	}

	@GetMapping("/2")
	public String endpoint2() {
		return "2";
	}

	@GetMapping("/3")
	public String endpoint3() {
		return "3";
	}

	@GetMapping("/call")
	public String callInSequence() {
		this.firstSpan = this.tracer.currentSpan();
		log.info("First");
		String first = webClient.get()
				.uri("/1")
				.retrieve()
				.bodyToMono(String.class)
				.block();
		this.postWebClientSpans.add(this.tracer.currentSpan());
		log.info("Second");
		String second = webClient.get()
				.uri("/2")
				.retrieve()
				.bodyToMono(String.class)
				.block();
		this.postWebClientSpans.add(this.tracer.currentSpan());
		log.info("Third");
		String third = webClient.get()
				.uri("/3")
				.retrieve()
				.bodyToMono(String.class)
				.block();
		this.postWebClientSpans.add(this.tracer.currentSpan());
		log.info("Fourth");
		return first + second + third;
	}
}

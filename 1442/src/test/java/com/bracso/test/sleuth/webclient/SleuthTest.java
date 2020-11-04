package com.bracso.test.sleuth.webclient;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class SleuthTest {

	private static final Logger LOG = LoggerFactory.getLogger(SleuthTest.class);

	@Autowired
	private WebClient webClient;

	@LocalServerPort
	private Integer localServerPort;

	@Test
	public void testWebClient() {
		final Flux<Employee> employeeFlux = this.webClient.get()
				.uri("http://localhost:" + localServerPort + "/employee").retrieve().bodyToFlux(Employee.class)
				.doOnEach((signal) -> {
					if (signal.isOnComplete()) {
						LOG.info("--> OnComplete");
					}
					else if (signal.isOnError()) {
						LOG.error("--> OnError", signal.getThrowable());
					}
					else if (signal.isOnNext()) {
						LOG.info("--> OnNext emp class = " + signal.get().getClass().getName());
						Employee emp = signal.get();
					}
				});

		employeeFlux.blockLast();

	}

}

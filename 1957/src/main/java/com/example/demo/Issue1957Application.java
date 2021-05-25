package com.example.demo;

import brave.http.HttpTracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.brave.ReactorNettyHttpTracing;

@SpringBootApplication
public class Issue1957Application {
	
	public static void main(String[] args) {
		
		// enable reactor netty access log
		System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");
		
		SpringApplication.run(Issue1957Application.class, args);
	}
	
	
	@RestController
	public static class Controller {
		private final Logger logger = LoggerFactory.getLogger(Controller.class);
		
		// access log contains spanId & traceId even without tracingCustomizer and reactor-netty-http-brave dependency
		@GetMapping("test1")
		public Mono<String> test1() {
			return Mono.just("response")
				.doOnNext(it -> logger.info("Test1"));
		}
		
		// access log doesn't contain spanId & traceId
		@GetMapping("test2")
		public Mono<String> test2() {
			return Mono.just("response")
				.doOnNext(it -> logger.info("Test2 - before publishOn"))
				.publishOn(Schedulers.boundedElastic())
				.doOnNext(it -> logger.info("Test2 - after publishOn"));
		}
		
		@Bean
		public NettyServerCustomizer tracingCustomizer(HttpTracing httpTracing) {
			return server -> ReactorNettyHttpTracing.create(httpTracing).decorateHttpServer(server);
		}
	}
}

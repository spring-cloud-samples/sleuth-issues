package com.example.w3c;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Issue2001Application {

	public static void main(String[] args) {
		SpringApplication.run(Issue2001Application.class, args);
	}
}

@RestController
class WebController {

	private final Tracer tracer;

	public WebController(Tracer tracer) {
		this.tracer = tracer;
	}

	@GetMapping("/")
	public String preference(@RequestHeader("tracestate") String tracestateHeader, @RequestHeader("baggage") String baggageHeader) {
		String tracestate = this.tracer.getBaggage("tracestate").get();
		if (tracestate == null) {
			throw new IllegalStateException("No tracestate as baggage");
		}
		String myBaggage = this.tracer.getBaggage("mybaggage").get();
		if (myBaggage == null) {
			throw new IllegalStateException("No mybaggage as baggage");
		}
		return "Headers tracestate: [" + tracestateHeader + "] baggage: [" + baggageHeader + "]. Baggage tracestate: [" + tracestate + "] mybaggage [" + myBaggage + "]";
	}
}

package com.example.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	SpanHandler arrayListSpanReporter() {
		return new TestSpanHandler();
	}
}

// Copied from Brave
class TestSpanHandler extends SpanHandler implements Iterable<MutableSpan> {
	// Synchronized not to discourage IntegrationTestSpanHandler when it should be used.
	// Rather, this allows iterative conversion of test code from custom Zipkin reporters to Brave.
	final List<MutableSpan> spans = new ArrayList<>(); // guarded by itself

	public MutableSpan get(int i) {
		synchronized (spans) {
			return spans.get(i);
		}
	}

	public List<MutableSpan> spans() {
		synchronized (spans) { // avoid Iterator pitfalls noted in Collections.synchronizedList
			return new ArrayList<>(spans);
		}
	}

	@Override
	public boolean end(TraceContext context, MutableSpan span, Cause cause) {
		synchronized (spans) {
			spans.add(span);
		}
		return true;
	}

	@Override
	public Iterator<MutableSpan> iterator() {
		return spans().iterator();
	}

	public void clear() {
		synchronized (spans) {
			spans.clear();
		}
	}

	@Override
	public String toString() {
		return "TestSpanHandler{" + spans() + "}";
	}
}

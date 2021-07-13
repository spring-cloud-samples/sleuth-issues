package com.example.demo;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ConfigurableApplicationContext;

@ExtendWith(OutputCaptureExtension.class)
public class Issue1990ApplicationTests {

	@Test
	void contextLoadsFails(CapturedOutput capturedOutput) {
		Assertions.assertThrows(BeanCreationException.class, () -> {
			Issue1990Application.main(new String[]{});
		});
		String output = capturedOutput.getAll();
		Assertions.assertTrue(output.contains("Caused by: java.lang.NullPointerException: topic == null"));
	}

	@Test
	void contextLoads(CapturedOutput capturedOutput) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Issue1990Application.class, "--app.config.reproduce.issue1990=false");
		String output = capturedOutput.getAll();
		Assertions.assertTrue(output.contains("Netty started on port"));
		SpringApplication.exit(ctx);
	}

}

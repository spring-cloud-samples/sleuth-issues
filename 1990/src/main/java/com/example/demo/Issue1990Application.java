package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.annotation.VaultPropertySource;

@SpringBootApplication
public class Issue1990Application {

	public static void main(String[] args) {
		SpringApplication.run(Issue1990Application.class, args);
	}

	@ConditionalOnProperty(value = "app.config.reproduce.issue1990", havingValue = "true")
	@Configuration
	@VaultPropertySource("my/clients/local")
	static class AppConfig {

	}
}

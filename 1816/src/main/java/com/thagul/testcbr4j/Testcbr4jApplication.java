package com.thagul.testcbr4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Testcbr4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(Testcbr4jApplication.class, args);
	}

}

package com.example.demo

import brave.sampler.Sampler
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

import static brave.sampler.Sampler.ALWAYS_SAMPLE

@SpringBootApplication
class DemoApplication {

	static void main(String[] args) {
		SpringApplication.run DemoApplication, args
	}

	@Bean
	Sampler sleuthTraceSampler() {
		ALWAYS_SAMPLE
	}

}

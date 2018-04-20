package com.github.goober.sleuthbug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class SleuthBugApplication {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public Mono<UserResponse> getUsers() {
		return userService.findUsers()
				.flatMap(user -> userService.getUserDetails(user.getId()))
				.reduce(UserResponse.builder(), (b, details) ->
					b.user(details)
				)
				.map(UserResponse.UserResponseBuilder::build);
	}

	public static void main(String[] args) {
		SpringApplication.run(SleuthBugApplication.class, args);
	}

	@Configuration
	public static class WebClientConfig {

		@Value("${server.port:7655}") int port;

		@Bean
		public WebClient webClient(WebClient.Builder webClientBuilder) {
			return webClientBuilder.baseUrl("http://localhost:" + this.port)
					.defaultHeader("Accept", "application/json")
					.build();
		}
	}

}

package com.github.goober.sleuthbug;

import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserClient {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserClient.class);
	@Autowired
    private WebClient webClient;

    Mono<BriefUserResponse> findUsers() {
        return this.webClient.get()
                .uri("/external").retrieve()
                .bodyToMono(BriefUserResponse.class);
    }

    Mono<UserDetails> getUserDetails(String id) {
        return this.webClient.get().uri("/external/{id}", id)
                .retrieve()
                .bodyToMono(UserDetails.class);
    }
}

package com.github.goober.sleuthbug;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserClient {

    @Autowired
    private WebClient webClient;

    Mono<BriefUserResponse> findUsers() {
        return webClient.get()
                .uri("/external").retrieve()
                .bodyToMono(BriefUserResponse.class);
    }

    Mono<UserDetails> getUserDetails(String id) {
        return webClient.get().uri("/external/{id}", id)
                .retrieve()
                .bodyToMono(UserDetails.class);
    }
}

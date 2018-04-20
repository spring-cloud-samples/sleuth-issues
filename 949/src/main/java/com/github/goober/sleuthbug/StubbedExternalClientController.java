package com.github.goober.sleuthbug;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RestController
@Slf4j
public class StubbedExternalClientController {

    @GetMapping(value = "/external", produces = "application/json")
    public Mono<BriefUserResponse> getBriefUsers() {
        BriefUserResponse response = BriefUserResponse.builder().userIds(Arrays.asList("1","2","3")).build();
        log.info("{}",response);
        return Mono.just(response);
    }

    @GetMapping(value= "/external/{id}", produces = "application/json")
    public Mono<UserDetails> getUserDetails(@PathVariable("id") String id) {
        return Mono.just(UserDetails.builder().id(id).name("test-user").build());
    }
}

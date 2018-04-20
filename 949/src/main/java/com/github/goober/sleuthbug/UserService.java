package com.github.goober.sleuthbug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserClient userClient;

    public Flux<User> findUsers() {
        return userClient.findUsers()
                .flatMapIterable(BriefUserResponse::getUserIds)
                .map(id -> User.builder().id(id).build());
    }

    public Mono<UserDetails> getUserDetails(String userId) {
        return userClient.getUserDetails(userId);
    }
}

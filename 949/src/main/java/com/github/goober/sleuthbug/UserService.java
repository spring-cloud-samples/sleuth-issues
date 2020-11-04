package com.github.goober.sleuthbug;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserClient userClient;

	public Flux<User> findUsers() {
		return this.userClient.findUsers()
				.flatMapIterable(BriefUserResponse::getUserIds)
				.map(id -> User.builder().id(id).build());
	}

	public Mono<UserDetails> getUserDetails(String userId) {
		return this.userClient.getUserDetails(userId);
	}
}

package com.github.goober.sleuthbug;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Builder
@Data
public class UserResponse {
    @Singular
    List<UserDetails> users;
}

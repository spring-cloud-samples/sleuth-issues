package com.github.goober.sleuthbug;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(onConstructor = @__({@JsonCreator}))
public class UserDetails {
    String id;
    String name;
}

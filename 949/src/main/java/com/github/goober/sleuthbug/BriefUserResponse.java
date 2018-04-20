package com.github.goober.sleuthbug;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__({@JsonCreator}))
public class BriefUserResponse {

    List<String> userIds;
}

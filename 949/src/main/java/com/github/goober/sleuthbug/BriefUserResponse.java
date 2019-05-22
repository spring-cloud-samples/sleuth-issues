package com.github.goober.sleuthbug;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public class BriefUserResponse {

    List<String> userIds;

    @JsonCreator
    @java.beans.ConstructorProperties({"userIds"})
    public BriefUserResponse(List<String> userIds) {
        this.userIds = userIds;
    }

    public static BriefUserResponseBuilder builder() {
        return new BriefUserResponseBuilder();
    }

    public List<String> getUserIds() {
        return this.userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BriefUserResponse)) return false;
        final BriefUserResponse other = (BriefUserResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$userIds = this.getUserIds();
        final Object other$userIds = other.getUserIds();
        if (this$userIds == null ? other$userIds != null : !this$userIds.equals(other$userIds)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BriefUserResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $userIds = this.getUserIds();
        result = result * PRIME + ($userIds == null ? 43 : $userIds.hashCode());
        return result;
    }

    public String toString() {
        return "BriefUserResponse(userIds=" + this.getUserIds() + ")";
    }

    public static class BriefUserResponseBuilder {
        private List<String> userIds;

        BriefUserResponseBuilder() {
        }

        public BriefUserResponse.BriefUserResponseBuilder userIds(List<String> userIds) {
            this.userIds = userIds;
            return this;
        }

        public BriefUserResponse build() {
            return new BriefUserResponse(userIds);
        }

        public String toString() {
            return "BriefUserResponse.BriefUserResponseBuilder(userIds=" + this.userIds + ")";
        }
    }
}

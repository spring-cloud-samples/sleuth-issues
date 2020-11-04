package com.github.goober.sleuthbug;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserDetails {
	String id;
	String name;

	@JsonCreator
	@java.beans.ConstructorProperties({"id", "name"})
	public UserDetails(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public static UserDetailsBuilder builder() {
		return new UserDetailsBuilder();
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof UserDetails)) return false;
		final UserDetails other = (UserDetails) o;
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
		return true;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		final Object $name = this.getName();
		result = result * PRIME + ($name == null ? 43 : $name.hashCode());
		return result;
	}

	public String toString() {
		return "UserDetails(id=" + this.getId() + ", name=" + this.getName() + ")";
	}

	public static class UserDetailsBuilder {
		private String id;
		private String name;

		UserDetailsBuilder() {
		}

		public UserDetails.UserDetailsBuilder id(String id) {
			this.id = id;
			return this;
		}

		public UserDetails.UserDetailsBuilder name(String name) {
			this.name = name;
			return this;
		}

		public UserDetails build() {
			return new UserDetails(id, name);
		}

		public String toString() {
			return "UserDetails.UserDetailsBuilder(id=" + this.id + ", name=" + this.name + ")";
		}
	}
}

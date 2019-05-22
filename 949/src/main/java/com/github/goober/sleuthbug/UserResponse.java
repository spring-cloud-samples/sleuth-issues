package com.github.goober.sleuthbug;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserResponse {
    List<UserDetails> users;

	@ConstructorProperties({"users"})
	UserResponse(List<UserDetails> users) {
		this.users = users;
	}

	public static UserResponseBuilder builder() {
		return new UserResponseBuilder();
	}

	public List<UserDetails> getUsers() {
		return this.users;
	}

	public void setUsers(List<UserDetails> users) {
		this.users = users;
	}

	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof UserResponse)) return false;
		final UserResponse other = (UserResponse) o;
		if (!other.canEqual((Object) this)) return false;
		final Object this$users = this.getUsers();
		final Object other$users = other.getUsers();
		if (this$users == null ? other$users != null : !this$users.equals(other$users)) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof UserResponse;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $users = this.getUsers();
		result = result * PRIME + ($users == null ? 43 : $users.hashCode());
		return result;
	}

	public String toString() {
		return "UserResponse(users=" + this.getUsers() + ")";
	}

	public static class UserResponseBuilder {
		private ArrayList<UserDetails> users;

		UserResponseBuilder() {
		}

		public UserResponse.UserResponseBuilder user(UserDetails user) {
			if (this.users == null) this.users = new ArrayList<UserDetails>();
			this.users.add(user);
			return this;
		}

		public UserResponse.UserResponseBuilder users(Collection<? extends UserDetails> users) {
			if (this.users == null) this.users = new ArrayList<UserDetails>();
			this.users.addAll(users);
			return this;
		}

		public UserResponse.UserResponseBuilder clearUsers() {
			if (this.users != null)
				this.users.clear();

			return this;
		}

		public UserResponse build() {
			List<UserDetails> users;
			switch (this.users == null ? 0 : this.users.size()) {
			case 0:
				users = java.util.Collections.emptyList();
				break;
			case 1:
				users = java.util.Collections.singletonList(this.users.get(0));
				break;
			default:
				users = java.util.Collections
						.unmodifiableList(new ArrayList<UserDetails>(this.users));
			}

			return new UserResponse(users);
		}

		public String toString() {
			return "UserResponse.UserResponseBuilder(users=" + this.users + ")";
		}
	}
}

package com.github.goober.sleuthbug;

public class User {
	String id;

	@java.beans.ConstructorProperties({"id"})
	User(String id) {
		this.id = id;
	}

	public static UserBuilder builder() {
		return new UserBuilder();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) return false;
		final User other = (User) o;
		if (!other.canEqual((Object) this)) return false;
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof User;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}

	public String toString() {
		return "User(id=" + this.getId() + ")";
	}

	public static class UserBuilder {
		private String id;

		UserBuilder() {
		}

		public User.UserBuilder id(String id) {
			this.id = id;
			return this;
		}

		public User build() {
			return new User(id);
		}

		public String toString() {
			return "User.UserBuilder(id=" + this.id + ")";
		}
	}
}

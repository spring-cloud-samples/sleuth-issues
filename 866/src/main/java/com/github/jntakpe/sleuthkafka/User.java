package com.github.jntakpe.sleuthkafka;

/**
 *
 */
public class User {

	private String username;

	private int age;

	public String getUsername() {
		return this.username;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public int getAge() {
		return this.age;
	}

	public User setAge(int age) {
		this.age = age;
		return this;
	}
}

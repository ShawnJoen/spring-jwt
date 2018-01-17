package com.jwt.spring_jwt;

import java.io.Serializable;

public class TestVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int userId;
	private String mobile;
	private String password;
	private String name;
	private String token;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "TestVO [userId=" + userId + ", name=" + name + ", mobile=" + mobile + ", token=" + token + "]";
	}
}
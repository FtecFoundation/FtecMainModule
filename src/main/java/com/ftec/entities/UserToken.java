package com.ftec.entities;

import java.util.Date;

public class UserToken {
	private String token;
	private Date expirationTime;
	
	public Date getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}

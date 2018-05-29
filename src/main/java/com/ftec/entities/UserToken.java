package com.ftec.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "usertokens")
public class UserToken {
	@Id
	private String token;
//	@Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern="yyyy-MM-dd HH:mm:ss +SSSS")
	private Date expirationTime;
	
	public UserToken(String token, Date expirationTime) {
		this.token = token;
		this.expirationTime = expirationTime;
	}
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

package com.ftec.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@Table(name = "token_table")
public class Token {

	@Id
	@Column(length = 30)
	private String token;
	private Date expirationTime;
	private long userId;

	public Token() {}

	public Token(String token, Date expirationTime) {
		this.token = token;
		this.expirationTime = expirationTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
}
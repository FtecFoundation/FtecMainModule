package com.ftec.entities;

import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
@IdClass(UserToken.class)
public class UserToken  implements Serializable{

	@Id
	private String token;
	@Id
	private Date expirationTime;
	
	public UserToken() {
		
	}
	public UserToken(String token, Date expirationTime) {
		this.token = token;
		this.expirationTime = expirationTime;
	}

	@Override
	public String toString() {
		return "UserToken{" +
				"token='" + token + '\'' +
				", expirationTime=" + expirationTime +
				'}';
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

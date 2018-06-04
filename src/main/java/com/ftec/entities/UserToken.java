package com.ftec.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table
@IdClass(UserToken.class)
public class UserToken  implements Serializable{

	@Id
	private String token;
	@Id
	private Date expirationTime;

	public UserToken() {}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserToken userToken = (UserToken) o;
		return Objects.equals(token, userToken.token) &&
				Objects.equals(expirationTime, userToken.expirationTime);
	}

	@Override
	public int hashCode() {

		return Objects.hash(token, expirationTime);
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
package com.ftec.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
@Data
public class UserToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TokenEmbId id;

	public UserToken() {}

	public UserToken(String token, Date expirationTime) {
		this.id = new TokenEmbId(token,expirationTime);
	}

	public Date getExpirationTime(){
		return id.getExpirationTime();
	}

	public String getToken(){
		return id.getToken();
	}
}
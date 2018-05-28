package com.ftec.repositories;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.ftec.entities.UserToken;
@Repository
public interface UserTokenDAO {
	public void addUserToken(String token, Date expirationDate);
	public UserToken getTokenEntity(String token);
}

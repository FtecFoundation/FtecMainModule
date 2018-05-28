package com.ftec.repositories;

import java.util.Date;

import org.springframework.stereotype.Repository;
@Repository
public interface UserTokenDAO {
	public void addUserToken(String token, Date expirationDate);
	public Date getTokenExpiration(String token);
}

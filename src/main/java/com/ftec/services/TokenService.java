package com.ftec.services;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ftec.entities.UserToken;
import com.ftec.exceptions.InvalidTokenException;
import com.ftec.repositories.UserTokenDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

@Service
public class TokenService {
	private static final String TOKEN_NAME = "TOKEN-X-AUTH";
	private final UserTokenDAO tokenManager;
	
	@Autowired
	public TokenService(UserTokenDAO tokenManager) {
		this.tokenManager = tokenManager;
	}
	
	public static Long getUserIdFromToken(HttpServletRequest request) {
		return getUserIdFromToken(getToken(request));
	}
	
	private static Long getUserIdFromToken(String token) throws InvalidTokenException{//throws Exception
		checkTokenFormat(token);
		return Long.valueOf(token.substring(0, token.indexOf("_")));
	}
	
	public static void checkTokenFormat(String token) {
		if(!token.contains("_")) throw new InvalidTokenException("Invalid token format! UserID_hash expected.");
		
		String userId = token.substring(0, token.indexOf("_"));
		
		try {
			Integer.valueOf(userId);
		}catch(Exception e) {
			throw new InvalidTokenException("Invalid token format! Exception while convert userID to Long.");
		}
		
	}

	private static String getToken(HttpServletRequest request) {
		return request.getHeader(TOKEN_NAME);
	}
	public static String generateToken(Long id) {
		return id.toString() + generateRandomString();
	}
	private static String generateRandomString() {
		int leftLimit = 97; 
	    int rightLimit = 122; 
	    int targetStringLength = 18;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	 
	    return "_" + generatedString;
	}
	
	public boolean isValidRequest(HttpServletRequest request) {
		UserToken tokenEntity = tokenManager.getByToken(getToken(request));
		Date expirationTime = tokenEntity.getExpirationTime();
		
		return expirationTime.after(new Date());
	}

}

package com.ftec.services;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftec.entities.UserToken;
import com.ftec.exceptions.InvalidTokenException;
import com.ftec.exceptions.NullTokenException;
import com.ftec.exceptions.TokenExpiredException;
import com.ftec.repositories.UserTokenDAO;

@Service
public class TokenService {
	
	public static final String TOKEN_NAME = "TOKEN-X-AUTH";
	private final UserTokenDAO tokenManager;
	
	@Autowired
	public TokenService(UserTokenDAO tokenManager) {
		this.tokenManager = tokenManager;
	}
	
	public static Long getUserIdFromToken(HttpServletRequest request) {
		return getUserIdFromToken(getToken(request));
	}

	private static Long getUserIdFromToken(String token) throws InvalidTokenException {
		checkTokenFormat(token);
		return Long.valueOf(extractUserID(token));
	}
	
	public static void checkTokenFormat(String token) {
		if(!token.contains("_")) throw new InvalidTokenException("Invalid token format! {UserID}_{Hash} expected.");
		
		String userId = extractUserID(token);
		
		try {
			Integer.valueOf(userId);
		} catch(Exception e) {
			throw new InvalidTokenException("Invalid token format! Exception while convert userID to Long.");
		}
		
	}

	public static String extractUserID(String token) {
		return token.substring(0, token.indexOf("_"));
	}

	private static String getToken(HttpServletRequest request) throws NullTokenException{
		String token = request.getHeader(TOKEN_NAME);
		
		if(token == null) throw new NullTokenException("No token in the header!");
		
		return token;
	}
	
	public String createSaveAndGetNewToken(Long id) {
		String token = generateToken(id);
		Date expiration = new Date();
		
		setExpirationTime(expiration); 
		tokenManager.save(new UserToken(token, expiration));
		
		return token;
	}

	public void setExpirationTime(Date expiration) {
		expiration.setTime(expiration.getTime() + 1800000);
	}

	public static String generateToken(Long id) {
		return id.toString() + "_" + generateRandomString();
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
	 
	    return generatedString;
	}
	
	public void verifyRequest(HttpServletRequest request) throws NullTokenException, TokenExpiredException{
		UserToken tokenEntity = tokenManager.getByToken(getToken(request));
				
		Date expirationTime = tokenEntity.getExpirationTime();
		
		if(!expirationTime.after(new Date())) throw new TokenExpiredException("Token has been expired!");	
	}

}

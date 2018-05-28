package com.ftec.services;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftec.entities.UserToken;
import com.ftec.exceptions.InvalidTokenException;
import com.ftec.repositories.UserTokenDAO;

@Service
public class TokenService {
	private static final String TOKEN_NAME = "TOKEN-X-AUTH";
	private static final int EXPIRATION_TIME = 1800;
	private final UserTokenDAO tokenManager;
	
	@Autowired
	public TokenService(UserTokenDAO tokenManager) {
		this.tokenManager = tokenManager;
	}
	
	public static Long getUserIdFromToken(HttpServletRequest request) {
		return getUserIdFromToken(getToken(request));
	}
	
	public static Long getUserIdFromToken(String token) throws InvalidTokenException{//throws Exception
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

	public static String getToken(HttpServletRequest request) {
		return request.getHeader(TOKEN_NAME);
	}
	
	public static void addTokenAfterSuccessfullLog(Long id, HttpServletResponse response) {
		String token = generateToken(id);
		Cookie cookie = new Cookie(TokenService.TOKEN_NAME, token);
        cookie.setMaxAge(EXPIRATION_TIME); 
        cookie.setPath("/");
        response.addCookie(cookie);
	}
	
	public static String generateToken(Long id) {
		return id.toString() + generateRandomString();
	}
	public static String generateRandomString() {
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
		UserToken tokenEntity = tokenManager.getTokenEntity(getToken(request));
		Date expirationTime = tokenEntity.getExpirationTime();
		
		return expirationTime.after(new Date());
	}
	
	public static void main(String[] args) {
		   	int leftLimit = 97; // letter 'a'
		    int rightLimit = 122; // letter 'z'
		    int targetStringLength = 18;
		    Random random = new Random();
		    StringBuilder buffer = new StringBuilder(targetStringLength);
		    for (int i = 0; i < targetStringLength; i++) {
		        int randomLimitedInt = leftLimit + (int) 
		          (random.nextFloat() * (rightLimit - leftLimit + 1));
		        buffer.append((char) randomLimitedInt);
		    }
		    String generatedString = buffer.toString();
		 
		    System.out.println("22_"+generatedString);
	}
	
}

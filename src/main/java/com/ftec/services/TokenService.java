package com.ftec.services;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftec.entities.UserToken;
import com.ftec.exceptions.token.InvalidTokenException;
import com.ftec.exceptions.token.NullTokenException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.exceptions.token.TokenExpiredException;
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
	
	static void checkTokenFormat(String token) {
		if(!token.contains("_")) throw new InvalidTokenException("Invalid token format! {UserID}_{Hash} expected.");
		
		String userId = extractUserID(token);
		
		try {
			Long.valueOf(userId);
		} catch(Exception e) {
			throw new InvalidTokenException("Invalid token format! Exception while convert userID to Long.");
		}
		
	}

	static String extractUserID(String token) {
		return token.substring(0, token.indexOf("_"));
	}

	public static String getToken(HttpServletRequest request) throws NullTokenException{
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

	private void setExpirationTime(Date expiration) {
		expiration.setTime(expiration.getTime() + 1800000);
	}

	static String generateToken(Long id) {
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
		return buffer.toString();
	}
	
	public void verifyToken(String token) throws TokenException{
		UserToken tokenEntity = getUserTokenFromRequest(token);
		
		Date expirationTime = tokenEntity.getExpirationTime();
		
		checkIfTokenExpired(expirationTime);	
	}

	private void checkIfTokenExpired(Date expirationTime) throws TokenExpiredException{
		if(!expirationTime.after(new Date())) throw new TokenExpiredException("Token has been expired!");
	}
	
	private UserToken getUserTokenFromRequest(String token) throws TokenException{
	    if(!tokenManager.findByIdToken(token).isPresent()) throw new TokenException("Token not found!");
		UserToken userToken = tokenManager.findByIdToken(token).get();
		
		return userToken;
	}
}

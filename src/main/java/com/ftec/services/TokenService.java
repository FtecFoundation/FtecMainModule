package com.ftec.services;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftec.repositories.UserTokenDAO;

@Service
public class TokenService {
	private static final String TOKEN_NAME = "TOKEN-X-AUTH";
	private static final int EXPIRATION_TIME = 1800;
	
	@Autowired
	private UserTokenDAO tokenManager;
	
	
	
	public static Long getUserIdFromToken(HttpServletRequest request) {
		String token = getToken(request.getCookies());
		return extractIdFromToken(token);
	}

	public static Long extractIdFromToken(String token) {
		return Long.valueOf(token.substring(0, token.indexOf("_")));
	}
	
	public static String getToken(Cookie[] cookies) {
		String token = null;
		
		for (Cookie cookie : cookies) {
			 if(cookie.getName().equals(TOKEN_NAME)) token = cookie.getValue();
		}
		return token;
	}
	
	
	public static void addTokenAfterSuccessfullLog(Long id, HttpServletResponse response) {
		String token = generateToken(id);
		Cookie cookie = new Cookie(TokenService.TOKEN_NAME, token);
        cookie.setMaxAge(EXPIRATION_TIME); 
        cookie.setPath("/");
        response.addCookie(cookie);
	}
	
	public static String generateToken(Long id) {
		String generatedString = RandomStringUtils.randomAlphanumeric(15);
		return id.toString() + "_" + generatedString;
	}
	
	public  boolean isValidToken(String token) {
		Date expirationTime = tokenManager.getTokenExpiration(token);
		return expirationTime.after(new Date());
	}
	
}

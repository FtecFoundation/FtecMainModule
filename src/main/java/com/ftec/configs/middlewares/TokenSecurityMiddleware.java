package com.ftec.configs.middlewares;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ftec.entities.Token;
import com.ftec.exceptions.token.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ftec.exceptions.token.TokenException;
import com.ftec.services.TokenService;

@Service
public class TokenSecurityMiddleware implements HandlerInterceptor{
	
	private final TokenService tokenService;

	@Autowired
	public TokenSecurityMiddleware(TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			String token = request.getHeader(TokenService.TOKEN_NAME);
			tokenService.verifyToken(token);
			tokenService.deleteExcessiveToken(TokenService.getUserIdFromToken(token));
		} catch(TokenException ex) {
			response.setStatus(403);
			return false;
		}
		return true;
	}

}

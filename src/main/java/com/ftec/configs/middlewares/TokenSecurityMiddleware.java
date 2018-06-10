package com.ftec.configs.middlewares;

import com.ftec.exceptions.token.TokenException;
import com.ftec.exceptions.token.TokenExpiredException;
import com.ftec.services.interfaces.TokenService;
import com.ftec.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class TokenSecurityMiddleware implements HandlerInterceptor{
	
	private final TokenService tokenService;

	@Autowired
	public TokenSecurityMiddleware(TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String token = request.getHeader(TokenService.TOKEN_NAME);
		try {
			tokenService.processToken(token);
		} catch(TokenException ex) {
			response.setStatus(403);
			if(isExpiredException(ex)) tokenService.deleteByToken(token);
			return false;
		} catch (Exception e){
			response.setStatus(403);
			Logger.logException("Unexpected exception in token security middleware!", e, true);
			return false;
		}
		return true;
	}

	public boolean isExpiredException(TokenException ex) {
		return ex.getClass().equals(TokenExpiredException.class);
	}

}

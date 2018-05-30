package com.ftec.configs.middlewares;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ftec.services.TokenService;

@Service
public class TokenSecurityMiddleware implements HandlerInterceptor{
	
	private final TokenService tokenService;

	@Autowired
	public TokenSecurityMiddleware(TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		try {		
			
			tokenService.verifyRequest(request);		
		
		} catch(RuntimeException ex) {
			response.setStatus(403);
			return false;
		}
		return true;
	}
	

}

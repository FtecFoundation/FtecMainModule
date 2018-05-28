package com.ftec.configs.middlewares;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ftec.services.TokenService;

@Service
public class TokenSecurityMiddleware implements HandlerInterceptor{
	@Autowired
	TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = tokenService.getToken(request.getCookies());
		return tokenService.isValidToken(token);
	}

}

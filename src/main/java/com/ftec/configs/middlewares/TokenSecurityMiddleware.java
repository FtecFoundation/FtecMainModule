package com.ftec.configs.middlewares;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.exceptions.token.TokenException;
import com.ftec.exceptions.token.TokenExpiredException;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.TokenService;
import com.ftec.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class TokenSecurityMiddleware implements HandlerInterceptor{
	
	private final TokenService tokenService;
    private final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	public TokenSecurityMiddleware(TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		String token = request.getHeader(TokenService.TOKEN_NAME);
		try {
			tokenService.processToken(token);

		} catch(TokenException ex) {
			response.setStatus(403);
			if(isExpiredException(ex)) tokenService.deleteByToken(token);
            Logger.logException("Expected exception in security middleware!", ex, true);

			response.getWriter().append(mapper.writeValueAsString(new MvcResponse(403, ex.getMessage())));
			return false;
		} catch (Exception e){
			response.setStatus(500);
			Logger.logException("Unexpected exception in token security middleware!", e, true);

            response.getWriter().append(mapper.writeValueAsString(new MvcResponse(500, "Unexpected exception in token security middleware!")));
            return false;
		}
		return true;
	}

	public boolean isExpiredException(TokenException ex) {
		return ex.getClass().equals(TokenExpiredException.class);
	}

}

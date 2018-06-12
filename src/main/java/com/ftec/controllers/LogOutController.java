package com.ftec.controllers;

import com.ftec.resources.enums.Statuses;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LogOutController {

	private final TokenService tokenService;

	@Autowired
	public LogOutController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	//should be secured access
	@PostMapping("/logout")
	public MvcResponse logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			tokenService.deleteByToken(request.getHeader(TokenService.TOKEN_NAME));
			return new MvcResponse(Statuses.Ok.getStatus());
		} catch(Exception e) {
		    response.sendError(500);
			return new MvcResponse(Statuses.UnexpectedError.getStatus());
		}
	}
}
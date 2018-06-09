package com.ftec.controllers;

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

	@PostMapping("/logout")
	public String logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			tokenService.deleteByToken(request.getHeader(TokenService.TOKEN_NAME));
			return "ok";
		} catch(Exception e) {
		    response.sendError(400);
		    return null;
		}
	}
}
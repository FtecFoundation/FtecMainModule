package com.ftec.controllers;

import com.ftec.repositories.TokenDAO;
import com.ftec.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LogOutController {

	private final TokenDAO tokenDAO;

	@Autowired
	public LogOutController(TokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}

	@PostMapping("/logout")
	public String logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			tokenDAO.deleteByToken(request.getHeader(TokenService.TOKEN_NAME));
			return "ok";
		} catch(Exception e) {
		    response.sendError(400);
		    return null;
		}
	}
}
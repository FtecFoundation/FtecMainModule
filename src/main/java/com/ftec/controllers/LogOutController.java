package com.ftec.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftec.repositories.UserTokenDAO;
import com.ftec.services.TokenService;

@RestController
public class LogOutController {
	
	private final UserTokenDAO tokenDAO;
	
	@Autowired
	public LogOutController(UserTokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}
	
	@GetMapping("/logout")
	public ResponseEntity<String> logOut(HttpServletRequest request){
		try {
			String token = TokenService.getToken(request);
			tokenDAO.deleteByToken(token);
		} catch(Exception e) {
		    return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.ACCEPTED);

	}
}

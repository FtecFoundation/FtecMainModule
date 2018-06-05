package com.ftec.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftec.entities.User;
import com.ftec.exceptions.AuthorizationException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.TokenService;

@RestController
public class AuthorizationController {
	public final static String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password!";

	private final TokenService tokenService;
	private final UserDAO userDAO;
	private final Environment environment;

	
	public 	static final String EMPTY_2FA_CODE_MESSAGE = "2Fa code is emply!";
	
	@Autowired
	public AuthorizationController(TokenService tokenService, UserDAO userDAO, Environment environment) {
			super();
			this.tokenService = tokenService;
			this.userDAO = userDAO;
			this.environment = environment;
		}
	
	@PostMapping("/authorization")
	public ResponseEntity<String> authorization(HttpServletResponse response,
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam(required = false) String twoStepVerCode) {
		
		try {
			Optional<User> userOpt = userDAO.findByUsername(username);	
			verifyIsUserFinded(userOpt);
			
			User user = userOpt.get();
			
			verify2FaCode(twoStepVerCode, user);
						
			if(isValidUsernamePassword(password, user)) {
				sendToken(response, user);
				return new ResponseEntity<>(HttpStatus.ACCEPTED);
			} 
			else throw new AuthorizationException(INVALID_USERNAME_OR_PASSWORD);
			
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
	}

	public void verify2FaCode(String twoStepVerCode, User user) throws AuthorizationException {
		if(isRequired2Fa(user)) check2FaCode(twoStepVerCode, user);
	}

	public Boolean isRequired2Fa(User user) {
		return user.getTwoStepVerification();
	}

	public void check2FaCode(String twoStepVerCode, User user) throws AuthorizationException{	
		
		if(twoStepVerCode == null || twoStepVerCode.length() == 0 ) throw new AuthorizationException(EMPTY_2FA_CODE_MESSAGE);
	
		if(this.environment.getActiveProfiles()[0].equals("test")) return;
		
		else throw new AuthorizationException("Test profile not selected!");

	}

	public void verifyIsUserFinded(Optional<User> userOpt) throws AuthorizationException {
		if(!userOpt.isPresent()) throw new AuthorizationException(INVALID_USERNAME_OR_PASSWORD);
	}

	public void sendToken(HttpServletResponse response, User user) {
		response.addHeader(TokenService.TOKEN_NAME, tokenService.createSaveAndGetNewToken(user.getId()));
	}

	public boolean isValidUsernamePassword(String password, User user) {
		return user.getPassword().equals(password);
	}
	
	//delete
}

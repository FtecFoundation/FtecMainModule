package com.ftec.controllers;

import com.ftec.entities.User;
import com.ftec.exceptions.AuthorizationException;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.TokenService;
import com.ftec.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class LoginController {
	final static String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password!";

	private final TokenService tokenService;
	private final UserDAO userDAO;
	private final Environment environment;

	
	static final String EMPTY_2FA_CODE_MESSAGE = "2Fa code is emply!";
	
	@Autowired
	public LoginController(TokenService tokenService, UserDAO userDAO, Environment environment) {
			super();
			this.tokenService = tokenService;
			this.userDAO = userDAO;
			this.environment = environment;
		}
	
	@PostMapping("/login")
	public MvcResponse authorization(HttpServletResponse response,
									 @RequestParam("username") String username,
									 @RequestParam("password") String password,
									 @RequestParam(value = "code", required = false, defaultValue = "") String twoStepVerCode) {
		
		try {
			Optional<User> userOpt = userDAO.findByUsername(username);	
			if(userOpt.isPresent() && PasswordUtils.isPasswordMatch(password, userOpt.get().getPassword(),userOpt.get().getSalt())) {
				if(userOpt.get().getTwoStepVerification()) check2FaCode(twoStepVerCode, userOpt.get());
				return new MvcResponse(200, "token", tokenService.createSaveAndGetNewToken(userOpt.get().getId()));
			} 
			else throw new AuthorizationException(INVALID_USERNAME_OR_PASSWORD);
			
		} catch(AuthorizationException e) {
			response.setStatus(403);
			return MvcResponse.getError(403, e.getMessage());
		}
		
		
	}

	private void check2FaCode(String twoStepVerCode, User user) throws AuthorizationException{
		if(twoStepVerCode == null || twoStepVerCode.length() == 0 ) throw new AuthorizationException(EMPTY_2FA_CODE_MESSAGE);
	
		if(this.environment.getActiveProfiles()[0].equals("test")) return;
		
		else throw new AuthorizationException("Test profile not selected!");

	}
}

package com.ftec.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ftec.entities.User;
import com.ftec.exceptions.AuthorizationException;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.enums.Statuses;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.TokenService;
import com.ftec.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class LoginController {
	final static String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password!";

	private final TokenService tokenService;
	private final UserDAO userDAO;
	private final Environment environment;

	
	static final String WRONG_2FA_CODE = "2Fa code is emply!";
	
	@Autowired
	public LoginController(TokenService tokenService, UserDAO userDAO, Environment environment) {
			super();
			this.tokenService = tokenService;
			this.userDAO = userDAO;
			this.environment = environment;
		}
	
	@PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
	public MvcResponse authorization(HttpServletResponse response, @RequestBody UserAuth userAuth) {
		Optional<User> userOpt = userDAO.findByUsername(userAuth.username);
		try {
			authorizate(userOpt,userAuth);
			
		} catch(AuthorizationException e) {
			response.setStatus(403);
			return MvcResponse.getMvcErrorResponse(Statuses.InvalidCredentials.getStatus(), e.getMessage());
		} catch (Exception e){
			response.setStatus(400);
			return MvcResponse.getMvcErrorResponse(400, "Unexpected error");
		}
		return new MvcResponse(200, "token", tokenService.createSaveAndGetNewToken(userOpt.get().getId()));
	}

	public void authorizate(Optional<User> userOpt, UserAuth userAuth) throws AuthorizationException {
		if(userOpt.isPresent() && PasswordUtils.isPasswordMatch(userAuth.password, userOpt.get().getPassword(),userOpt.get().getSalt())) {
			check2FaCode(userAuth.code, userOpt.get());
		} else throw new AuthorizationException(INVALID_USERNAME_OR_PASSWORD);
	}

	private void check2FaCode(String twoStepVerCode, User user) throws AuthorizationException {
		if(!user.getTwoStepVerification()) return;

		if(twoStepVerCode == null || twoStepVerCode.length() == 0 ) throw new AuthorizationException(WRONG_2FA_CODE);
	
		for(String profile: this.environment.getActiveProfiles()){
			if(profile.equals("test")) return;
		}

		throw new AuthorizationException(WRONG_2FA_CODE);

	}

	private static class UserAuth{
		private String username;
		private String password;
		@JsonProperty(defaultValue = "")
		private String code;

		public UserAuth() {
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}
}

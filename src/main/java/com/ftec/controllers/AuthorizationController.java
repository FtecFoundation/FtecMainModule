package com.ftec.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ftec.entities.User;
import com.ftec.exceptions.AuthorizationException;
import com.ftec.exceptions.TwoStepVerificationException;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.enums.Statuses;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.AuthorizationService;
import com.ftec.services.interfaces.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class AuthorizationController {

	private final TokenService tokenService;
	private final UserDAO userDAO;
    private final AuthorizationService authorizationService;

	@Autowired
	public AuthorizationController(TokenService tokenService, UserDAO userDAO, AuthorizationService authorizationService) {
			super();
			this.tokenService = tokenService;
			this.userDAO = userDAO;
            this.authorizationService = authorizationService;
    }
	
	@PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
	public MvcResponse authorization(HttpServletResponse response, @RequestBody UserAuth userAuth) {
	    Optional<User> userOpt = userDAO.findByUsername(userAuth.username);
	    long userId;

		try {
            authorizationService.authorizate(userOpt,userAuth);
			userId = userOpt.get().getId();
		} catch(AuthorizationException e) {
			response.setStatus(403);
			return MvcResponse.getMvcErrorResponse(Statuses.InvalidCredentials.getStatus(), e.getMessage());

		} catch (TwoStepVerificationException e){
            response.setStatus(403);
            return MvcResponse.getMvcErrorResponse(Statuses.Invalid2FA.getStatus(), e.getMessage());

        } catch (Exception e) {
			response.setStatus(500);
			return MvcResponse.getMvcErrorResponse(Statuses.UnexpectedError.getStatus(), "Unexpected error");
		}
		return new MvcResponse(Statuses.Ok.getStatus(), "token", tokenService.createSaveAndGetNewToken(userId));
	}




	public static class UserAuth{
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

package com.ftec.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ftec.constratints.UniqueEmail;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.TokenService;

import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
public class ChangeSettingController {
	private final UserDAO userDAO;

	@Autowired
	public ChangeSettingController(UserDAO userDao) {
		this.userDAO = userDao;
	}
	
	@PostMapping("/changeUserSetting")
	public MvcResponse changeUserSetting(@RequestBody @Valid UserUpdate userUpdate, BindingResult br, HttpServletRequest request, HttpServletResponse response) {

		if(br.hasErrors()) { return new MvcResponse(set400Status(response),br.getAllErrors());}
			
		User userFromDB = userDAO.findById(TokenService.getUserIdFromToken(request)).get();
		
		userFromDB.apllyChangeSettings(userUpdate);
		
		userDAO.save(userFromDB);
		return new MvcResponse(200);
	}

	public static int set400Status(HttpServletResponse response) {
		response.setStatus(400);
		return 400;
	}

	@Data
	@NoArgsConstructor
	public static class UserUpdate{

		
		@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
		private String password;
		
		@Email
		@UniqueEmail
		private String email;
		
		private Boolean twoFactorEnabled;
		
		private Boolean subscribeForNews;
	}
	
}

package com.ftec.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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

import java.util.stream.Collectors;

@RestController
public class ChangeSettingController {
	private final UserDAO userDAO;

	@Autowired
	public ChangeSettingController(UserDAO userDao) {
		this.userDAO = userDao;
	}
	
	@PostMapping("/changeUserSetting")
	public MvcResponse changeUserSetting(@RequestBody @Valid UserUpdate userUpdate, BindingResult br, HttpServletRequest request, HttpServletResponse response) {

		if(br.hasErrors()) {
		    response.setStatus(400);
		    return MvcResponse.getMvcResponse(400,br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("")));
		}

		User userFromDB = userDAO.findById(TokenService.getUserIdFromToken(request)).get();
		
		userFromDB.apllyChangeSettings(userUpdate);
		
		userDAO.save(userFromDB);
		return new MvcResponse(200);
	}


	@Data
	@NoArgsConstructor
	public static class UserUpdate {
		//each field could be null
		@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
        @Size(max = 20)
		private String password;

		@Email
		@UniqueEmail
        @Size(max = 20)
		private String email;

		private Boolean twoFactorEnabled;

		private Boolean subscribeForNews;
	}
	
}

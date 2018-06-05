package com.ftec.controllers;

import com.ftec.constratints.UniqueEmail;
import com.ftec.exceptions.UserNotExistsException;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.ChangeSettingsService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.stream.Collectors;

@RestController
public class ChangeSettingController {
	private final ChangeSettingsService changeSettingsService;

	public ChangeSettingController(ChangeSettingsService changeSettingsService) {
		this.changeSettingsService = changeSettingsService;
	}


	@PostMapping("/changeUserSetting")
	public MvcResponse changeUserSetting(@RequestBody @Valid UserUpdate userUpdate, BindingResult br, HttpServletRequest request, HttpServletResponse response) {
		if(br.hasErrors()) {
		    response.setStatus(400);
		    return MvcResponse.getError(400,br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("")));
		}
		try {
			changeSettingsService.updatePreferences(userUpdate, TokenService.getUserIdFromToken(request));
		}catch (UserNotExistsException ex){
			response.setStatus(400);
			return MvcResponse.getError(400, "NoUserExists");
		}
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

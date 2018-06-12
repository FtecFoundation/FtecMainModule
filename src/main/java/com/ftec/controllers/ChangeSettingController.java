package com.ftec.controllers;

import com.ftec.constratints.Patterns;
import com.ftec.constratints.UniqueEmail;
import com.ftec.exceptions.UserNotExistsException;
import com.ftec.resources.enums.Statuses;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.ChangeSettingsService;
import com.ftec.services.interfaces.TokenService;
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

	public static final String CHANGE_USER_SETTING_URL = "/changeUserSetting";

	public ChangeSettingController(ChangeSettingsService changeSettingsService) {
		this.changeSettingsService = changeSettingsService;
	}

	@PostMapping(value = CHANGE_USER_SETTING_URL, produces = "application/json")
	public MvcResponse changeUserSetting(@RequestBody @Valid UserUpdate userUpdate, BindingResult br, HttpServletRequest request, HttpServletResponse response) {
		if(br.hasErrors()) {
		    response.setStatus(400);
		    return MvcResponse.getMvcErrorResponse(Statuses.InvalidCredentials.getStatus(), br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("")));
		}
		try {
			changeSettingsService.updatePreferences(userUpdate, TokenService.getUserIdFromToken(request.getHeader(TokenService.TOKEN_NAME)));
		} catch (UserNotExistsException ex){
			response.setStatus(400);
			return MvcResponse.getMvcErrorResponse(Statuses.UserNotExist.getStatus(), ex.getMessage());
		}
		return new MvcResponse(Statuses.Ok.getStatus());
	}


	@Data
	@NoArgsConstructor
	public static class UserUpdate {

        @Pattern(regexp = Patterns.PASSWORD_PATTERN, message = "Password must have symbols in uppercase, symbols in lowercase and number")
        @Size(min = 4, max = 20, message = "The length of the password must be more than 4 and less than 20")
		private String password;

        @Email(message = "Email format is incorrect")
        @UniqueEmail(message = "This email already taken")
        @Size(max = 40, message = "Maximum email length is 40 symbols")
		private String email;

		private Boolean twoFactorEnabled;

		private Boolean subscribeForEmail;
	}
	
}

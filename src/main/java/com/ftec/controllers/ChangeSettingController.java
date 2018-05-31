package com.ftec.controllers;

import com.ftec.entities.MvcResponse;
import com.ftec.entities.User;
import com.ftec.exceptions.InvalidUpdateDataException;
import com.ftec.repositories.UserDAO;
import com.ftec.utils.EmailRegexp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@RestController
public class ChangeSettingController {
	private final UserDAO userDao;

	@Autowired
	public ChangeSettingController(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@PostMapping("/changeUserSetting")
	public ResponseEntity<String> changeUserSetting(@Valid UserUpdate userUpdate, BindingResult br) {
		if(br.hasErrors()){
			//TODO change mvc response to shortened version
			return new MvcResponse(400, br.getAllErrors());
		}

	}


	public void checkAndUpdateTwoStepVerification(User user, User newUserSettings) {
		Boolean updatedTwoStepVerif = newUserSettings.isTwoStepVerification();
		if(updatedTwoStepVerif != null) {
			user.setTwoStepVerification(updatedTwoStepVerif);
		}
	}

	public void checkAndUpdatePassword(User user, User newUserSettings) throws InvalidUpdateDataException{
		if(newUserSettings.getPassword() != null) {
			//TODO add regexp, if password not valid throw new InvalidUpdateDataException("Invalid password!");
			user.setPassword(newUserSettings.getPassword());
		}
	}

	public boolean checkAndUpdateEmail(User user, User newUserSettings) throws InvalidUpdateDataException {

		String new_email = newUserSettings.getEmail();
		if(isEmailNull(new_email) || isTheSameEmail(user, new_email)) return false;

		if(isValidEmail(new_email)) {

				if(isDublicateEmail(new_email)) {
					throw new InvalidUpdateDataException("User with same email already exist");
				}

			user.setEmail(new_email);
			return true;

		} else return false;

	}




	public boolean isEmailNull(String new_email) {
		return new_email == null;
	}

	public boolean isDublicateEmail(String new_email) {
		return userDao.findByEmail(new_email).isPresent();
	}

	public boolean isTheSameEmail(User user, String new_email) {
		return new_email.equals(user.getEmail());
	}

	public boolean isValidEmail(String new_email) {
		return EmailRegexp.validate(new_email);
	}

	static class UserUpdate{
		@Null
		@Min(0)
		private long userId;
		@Null
		@Pattern(regexp = "")
		private String password;
		@Null
		@Pattern(regexp = "")
		private String email;
		@Null
		private boolean isTwoFactorEnabled;

	}
	
}

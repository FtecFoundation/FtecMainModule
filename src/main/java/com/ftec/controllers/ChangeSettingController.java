package com.ftec.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ftec.entities.User;
import com.ftec.exceptions.InvalidUpdateDataException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.TokenService;
import com.ftec.utils.EmailRegexp;

@RestController
public class ChangeSettingController {
	private final UserDAO userDao;

	@Autowired
	public ChangeSettingController(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@PostMapping("/changeUserSetting")
	public ResponseEntity<String> changeUserSetting(HttpServletRequest request, @RequestBody User newUserSettings) {
	
		Optional<User> userFromDB_opt = userDao.findById(TokenService.getUserIdFromToken(request));

		if(!userFromDB_opt.isPresent()) return new ResponseEntity<String>("User with this id does not exist in DB", HttpStatus.BAD_REQUEST);
		
		User user = userFromDB_opt.get();
	
		
		try {
			checkAndUpdateEmail(user, newUserSettings);
			
			checkAndUpdatePassword(user, newUserSettings);
			
			checkAndUpdateTwoStepVerification(user, newUserSettings);
			
			userDao.save(user);
			
		}catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
	
	
	
}

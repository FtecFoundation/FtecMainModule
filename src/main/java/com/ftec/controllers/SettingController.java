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
public class SettingController {
	private final UserDAO userDao;

	@Autowired
	public SettingController(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@PostMapping("/changeUserSetting")
	public ResponseEntity<String> changeUserSetting(HttpServletRequest request, @RequestBody User newUserSettings) {
	
		Optional<User> userFromDB_opt = userDao.findById(TokenService.getUserIdFromToken(request));

		if(!userFromDB_opt.isPresent()) return new ResponseEntity<String>("User with this id does not exist in DB", HttpStatus.BAD_REQUEST);
		
		long userId = userFromDB_opt.get().getId();
		newUserSettings.setId(userId);
	
		try {
		checkAndUpdateEmail(newUserSettings);
		
		checkAndUpdatePassword(newUserSettings);
		
		checkAndUpdateTwoStepVerification(newUserSettings);
		}catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public void checkAndUpdateTwoStepVerification(User newUserSettings) {
		if(newUserSettings.isTwoStepVerification() != null) {
			//userDao.updateTwoStepVerificationById(userId, newUserSettings.isTwoStepVerification());
		}
	}

	public void checkAndUpdatePassword(User newUserSettings) throws InvalidUpdateDataException{
		if(newUserSettings.getPassword() != null) {
			//if password not valid throw new InvalidUpdateDataException("Invalid password!");
			//userDao.updatePasswordById(userId, newUserSettings.getPassword());
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void checkAndUpdateEmail(User newUserSettings) throws InvalidUpdateDataException {
		//check wether the user want to change his email
		String new_email = newUserSettings.getEmail();
		if(new_email != null && EmailRegexp.validate(new_email)) {
				
				if(userDao.findByEmail(new_email).isPresent()) {
					throw new InvalidUpdateDataException("User with same email already exist");
				}
			
				//userDao.updateEmailById(userId, newUserSettings.getEmail());
		}
	}
	
	
	
}

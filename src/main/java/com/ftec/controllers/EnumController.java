package com.ftec.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftec.configs.enums.TutorialSteps;
import com.ftec.entities.MvcResponse;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.services.TokenService;

@RestController
public class EnumController {
	private final UserDAO userDao;
	
	@Autowired
	public EnumController(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@PostMapping("/nextStep")
	public TutorialSteps nextStep(HttpServletRequest request) throws NullPointerException {
		Long userId = TokenService.getUserIdFromToken(request);
		User user = userDao.findById(userId).get();
		TutorialSteps.setNextStep(user);
		return user.getStep();
	}
	
	@GetMapping("/getCurrentStep")
	public TutorialSteps getCurrentStep(HttpServletRequest request) throws NullPointerException {
		Long userId = TokenService.getUserIdFromToken(request);
		User user = userDao.findById(userId).get();
		return user.getStep();		
	}
	
	@GetMapping("/securedTest")
	public ResponseEntity<?> testSecuredPage() {
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/testMvcResponse")
	public MvcResponse testResponse() {
		User u = new User();
		u.setCurrentStep(TutorialSteps.SECOND);
		u.setEmail("email");
		u.setId(25L);
		MvcResponse<User> response = new MvcResponse<>();
		response.setResponse(u);
		response.setStatus(HttpStatus.OK);
		response.setError("some error");
		return response;
	}
}


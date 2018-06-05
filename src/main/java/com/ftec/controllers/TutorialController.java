package com.ftec.controllers;

import com.ftec.configs.enums.TutorialSteps;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TutorialController {
	private final UserDAO userDao;

	@Autowired
	public TutorialController(UserDAO userDao) {
		this.userDao = userDao;
	}

	@PostMapping("/cabinet/tutorial/nextStep")
	public TutorialSteps nextStep(HttpServletRequest request) throws NullPointerException {
		Long userId = TokenService.getUserIdFromToken(request);
		User user = userDao.findById(userId).get();
		TutorialSteps.setNextStep(user);
		return user.getCurrentStep();
	}

	@GetMapping("/cabinet/tutorial/getCurrentStep")
	public TutorialSteps getCurrentStep(HttpServletRequest request) throws NullPointerException {
		Long userId = TokenService.getUserIdFromToken(request);
		User user = userDao.findById(userId).get();
		return user.getCurrentStep();
	}

}

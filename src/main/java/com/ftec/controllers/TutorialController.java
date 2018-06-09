package com.ftec.controllers;

import com.ftec.exceptions.TutorialCompletedException;
import com.ftec.resources.enums.TutorialSteps;
import com.ftec.services.interfaces.TokenService;
import com.ftec.services.interfaces.TutorialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TutorialController {
	private final TutorialService tutorialService;

	public TutorialController(TutorialService tutorialService) {
		this.tutorialService = tutorialService;
	}

	@PostMapping("/cabinet/tutorial/nextStep")
	public TutorialSteps nextStep(HttpServletRequest request) throws NullPointerException, TutorialCompletedException {
		return tutorialService.proceedToNextStep(TokenService.getUserIdFromToken(request.getHeader(TokenService.TOKEN_NAME)));
	}

	@GetMapping("/cabinet/tutorial/getCurrentStep")
	public TutorialSteps getCurrentStep(HttpServletRequest request) throws NullPointerException {
		return tutorialService.getCurrentStep(TokenService.getUserIdFromToken(request.getHeader(TokenService.TOKEN_NAME)));
	}

}

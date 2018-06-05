package com.ftec.services.interfaces;

import com.ftec.configs.enums.TutorialSteps;
import com.ftec.entities.User;
import com.ftec.exceptions.TutorialCompletedException;

public interface TutorialService {
    TutorialSteps proceedToNextStep(User u) throws TutorialCompletedException;
    TutorialSteps proceedToNextStep(long userId) throws TutorialCompletedException;
    TutorialSteps getCurrentStep(long userId);
}

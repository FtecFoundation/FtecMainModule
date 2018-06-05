package com.ftec.services.Implementations;

import com.ftec.resources.enums.TutorialSteps;
import com.ftec.entities.User;
import com.ftec.exceptions.TutorialCompletedException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.TutorialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TutorialServiceImpl implements TutorialService {
    private final UserDAO userDAO;

    public TutorialServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    @Override
    public TutorialSteps proceedToNextStep(User u) throws TutorialCompletedException {
        TutorialSteps nextStep = u.getCurrentStep().getNextStep();
        if(nextStep == null) throw new TutorialCompletedException();
        userDAO.updateTutorialStepForUser(u.getId(), u.getCurrentStep().getNextStep());
        return u.getCurrentStep().getNextStep();
    }

    @Transactional
    @Override
    public TutorialSteps proceedToNextStep(long userId) throws TutorialCompletedException{
        Optional<User> user = userDAO.findById(userId);
        if(!user.isPresent()) throw new NullPointerException();
        TutorialSteps nextStep = user.get().getCurrentStep().getNextStep();
        if(nextStep == null) throw new TutorialCompletedException();
        userDAO.updateTutorialStepForUser(userId, nextStep);
        return nextStep;
    }

    @Override
    public TutorialSteps getCurrentStep(long userId) {
        return userDAO.getTutorialStep(userId);
    }
}

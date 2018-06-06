package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.exceptions.TutorialCompletedException;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.enums.TutorialSteps;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.TutorialService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class TutorialTest {
    @Autowired
    TutorialService tutorialService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserDAO userDAO;

    @Test
    public void testRegisteredStep() {
        User u = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(u);
        assertEquals(tutorialService.getCurrentStep(u.getId()),(TutorialSteps.FIRST));
    }

    @Test
    public void testChangeToNextStep() throws TutorialCompletedException {
        User u = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(u);

        tutorialService.proceedToNextStep(u.getId());
        assertEquals(TutorialSteps.SECOND, userDAO.findById(u.getId()).get().getCurrentStep());
    }

    @Test(expected = TutorialCompletedException.class)
    public void endTutorial() throws TutorialCompletedException {
        User u = EntityGenerator.getNewUser();
        u.setCurrentStep(TutorialSteps.THIRD);
        registrationService.registerNewUserAccount(u);
        tutorialService.proceedToNextStep(u.getId());
    }
}

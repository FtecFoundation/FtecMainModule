package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.resources.enums.TutorialSteps;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.TutorialService;
import com.ftec.services.interfaces.UserService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class TutorialTest {
    @Autowired
    TutorialService tutorialService;
    @Autowired
    RegistrationService registrationService;


    @Test
    public void testRegisteredStep() throws UserExistException {
        User u = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(u);
        assertEquals(tutorialService.getCurrentStep(u.getId()),(TutorialSteps.FIRST));
    }
}

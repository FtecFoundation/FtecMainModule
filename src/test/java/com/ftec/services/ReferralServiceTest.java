package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.RegistrationController;
import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.User;
import com.ftec.services.interfaces.ReferralService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ReferralServiceTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserService userService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    ReferralService referralService;

    @Test
    public void testSaveReferralSystem() throws Exception {
        //Saving Users without referrer Id (Main users)
        RegistrationController.UserRegistration userRegWithoutReferrer1 = new RegistrationController.UserRegistration("testUser1", "Strong_Pass1", "email1@gmail.com", false);
        RegistrationController.UserRegistration userRegWithoutReferrer2 = new RegistrationController.UserRegistration("testUser2", "Strong_Pass2", "email2@gmail.com", false);

        User userWithoutReferrer1 = registrationService.registerUser(userRegWithoutReferrer1);
        User userWithoutReferrer2 = registrationService.registerUser(userRegWithoutReferrer2);

        userService.registerNewUserAccount(userWithoutReferrer1);
        userService.registerNewUserAccount(userWithoutReferrer2);

        //Saving Users with referrer Id (Sub users)
        RegistrationController.UserRegistration userRegWithReferrer1 = new RegistrationController.UserRegistration("testUser1", "Strong_Pass1", "email1@gmail.com", false, userWithoutReferrer1.getId());
        RegistrationController.UserRegistration userRegWithReferrer2 = new RegistrationController.UserRegistration("testUser2", "Strong_Pass2", "email2@gmail.com", false, userWithoutReferrer1.getId());
        RegistrationController.UserRegistration userRegWithReferrer3 = new RegistrationController.UserRegistration("testUser3", "Strong_Pass3", "email3@gmail.com", false, userWithoutReferrer1.getId());
        RegistrationController.UserRegistration userRegWithReferrer4 = new RegistrationController.UserRegistration("testUser4", "Strong_Pass4", "email4@gmail.com", false, userWithoutReferrer2.getId());
        RegistrationController.UserRegistration userRegWithReferrer5 = new RegistrationController.UserRegistration("testUser4", "Strong_Pass4", "email4@gmail.com", false, userWithoutReferrer2.getId());

        User userWithReferrer1 = registrationService.registerUser(userRegWithReferrer1);
        User userWithReferrer2 = registrationService.registerUser(userRegWithReferrer2);
        User userWithReferrer3 = registrationService.registerUser(userRegWithReferrer3);
        User userWithReferrer4 = registrationService.registerUser(userRegWithReferrer4);
        User userWithReferrer5 = registrationService.registerUser(userRegWithReferrer5);

        userService.registerNewUserAccount(userWithReferrer1);
        userService.registerNewUserAccount(userWithReferrer2);
        userService.registerNewUserAccount(userWithReferrer3);
        userService.registerNewUserAccount(userWithReferrer4);
        userService.registerNewUserAccount(userWithReferrer5);

//        referralService.saveReferralLevelOne(new ReferralLevelOne());
    }
}


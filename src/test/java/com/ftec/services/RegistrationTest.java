package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.services.RegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
@ActiveProfiles("credentials")
public class RegistrationTest {
    @Autowired
    public RegistrationService registrationService;

    @Test
    public void testRegistration() {
        String username = "testUsername";
//        RegistrationController.RegistrationUser preRegisteredUser =
//                new RegistrationController.RegistrationUser(username,"password","test@email.com",0, true);
//        long id = registrationService.registerUser(preRegisteredUser);
        //In registration service only temporary, will be moved to next one;
//        assert registrationService.getUser(id).getLogin().equals(username);
//        assert registrationService.deleteUser(id);
    }
}

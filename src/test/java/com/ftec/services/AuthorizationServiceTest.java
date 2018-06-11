package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.LoginController;
import com.ftec.entities.User;
import com.ftec.exceptions.AuthorizationException;
import com.ftec.services.interfaces.AuthorizationService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class AuthorizationServiceTest {

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    RegistrationService registrationService;

    @Test
    public void authorizationModuleTest() throws AuthorizationException {
        User u = EntityGenerator.getNewUser();
        String raw_pass = u.getPassword();
        registrationService.registerNewUserAccount(u);

        Optional<User> optional = Optional.of(u);
        LoginController.UserAuth userAuth = new LoginController.UserAuth();
        userAuth.setUsername(u.getUsername());
        userAuth.setPassword(raw_pass);

        authorizationService.authorizate(optional, userAuth);
    }
}

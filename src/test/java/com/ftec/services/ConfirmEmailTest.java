package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ConfirmEmailTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ConfirmEmailService confirmEmailService;

    @Test
    public void test(){
        User user = EntityGenerator.getNewUser();
        user.setEmail(Resources.sendToStatic != null ? Resources.sendToStatic : user.getEmail());

        registrationService.registerNewUserAccount(user);

        assertTrue(userDAO.findByUsername(user.getUsername()).isPresent());
        assertFalse(userDAO.findByUsername(user.getUsername()).get().isConfirmedEmail());

        confirmEmailService.sendConfirmEmailUrl(user.getEmail(), user.getId());


    }
}

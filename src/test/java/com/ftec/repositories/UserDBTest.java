package com.ftec.repositories;

import com.ftec.configs.ApplicationConfig;
import com.ftec.resources.enums.TutorialSteps;
import com.ftec.entities.User;
import com.ftec.utils.EntityGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ActiveProfiles(value = "test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
public class UserDBTest {

    @Autowired
    UserDAO userDAO;

    @Before
    public void setUp(){
        userDAO.deleteAll();
    }

    @Test
    public void userDBtest() {

        User u = EntityGenerator.getNewUser();

        String username = u.getUsername();
        String password = u.getPassword();
        String email = u.getEmail();
        TutorialSteps currentStep = TutorialSteps.FIRST;

        userDAO.save(u);
        long id = u.getId();

        assertTrue(userDAO.findById(id).get().getUsername().equals(username));
        assertTrue(userDAO.findByUsername(username).get().getPassword().equals(password));
        assertTrue(userDAO.findByEmail(email).get().getUsername().equals(username));
        assertTrue(userDAO.findByCurrentStep(currentStep).get().getUsername().equals(username));

        User u2 = EntityGenerator.getNewUser();
        userDAO.save(u2);
        assertEquals(u2.getId() - u.getId(),1);
    }
}

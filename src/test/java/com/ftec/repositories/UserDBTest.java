package com.ftec.repositories;

import com.ftec.configs.ApplicationConfig;
import com.ftec.configs.enums.TutorialSteps;
import com.ftec.entities.User;
import com.ftec.services.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
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
        assertFalse(userDAO.findAll().iterator().hasNext()); //isEmptyTable


        String username = "user1";
        String password = "pass1";
        String email = "email@d.net";
        TutorialSteps currentStep = TutorialSteps.FIRST;
        boolean subscribeForNews = false;
        Boolean twoStepVerification = false;

        User u = new User(username, password,
                email, currentStep, subscribeForNews, twoStepVerification);
        userDAO.save(u);
        long id = u.getId();
        //works with 1 user
        assertTrue(userDAO.findById(id).get().getUsername().equals(username));
        assertTrue(userDAO.findByUsername(username).get().getPassword().equals(password));
        assertTrue(userDAO.findByEmail(email).get().getUsername().equals(username));
        assertTrue(userDAO.findByCurrentStep(currentStep).get().getUsername().equals(username));

        User u2 = new User("name","passs","user2",currentStep,true,false);
        userDAO.save(u2);
    }
}

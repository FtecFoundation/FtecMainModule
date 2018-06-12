package com.ftec.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;
import com.ftec.repositories.ConfirmDataDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.resources.enums.ConfirmScope;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ConfirmEmailTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ConfirmDataDAO confirmDataDAO;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void confirmTest() throws Exception {

        User user = EntityGenerator.getNewUser();
        user.setEmail(Resources.sendToStatic != null ? Resources.sendToStatic : user.getEmail());

        RegistrationController.UserRegistration registration = new RegistrationController.UserRegistration();
        registration.setEmail(user.getEmail());
        registration.setUsername(user.getUsername());
        registration.setPassword(user.getPassword());

        mvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(registration)))
                .andExpect(status().is(200)).andReturn();

        long id = userDAO.findByUsername(registration.getUsername()).get().getId();

        assertTrue(confirmDataDAO.findByUserIdAndScope(id, ConfirmScope.ConfirmEmail).isPresent());

        assertFalse(userDAO.findByUsername(user.getUsername()).get().isConfirmedEmail());

        String hash = confirmDataDAO.findByUserIdAndScope(id, ConfirmScope.ConfirmEmail).get().getHash();

        mvc.perform(post("/confirmEmail?hash="+hash)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200)).andReturn();

        assertTrue(userDAO.findByUsername(user.getUsername()).get().isConfirmedEmail());

    }


}

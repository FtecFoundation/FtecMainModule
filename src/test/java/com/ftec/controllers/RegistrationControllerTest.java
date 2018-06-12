package com.ftec.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.services.Implementations.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserServiceImpl userService;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void trySaveNullUsername() throws Exception {
        RegistrationController.UserRegistration userRegistration = new RegistrationController.UserRegistration();
        userRegistration.setUsername(null);
        userRegistration.setPassword("NullUser_Pass_228");
        userRegistration.setEmail("NullUsername_@gmail.com");

        //should be status 400
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(objectMapper.writeValueAsString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(400));

        assertFalse(userDAO.findByEmail(userRegistration.getEmail()).isPresent());
    }

}

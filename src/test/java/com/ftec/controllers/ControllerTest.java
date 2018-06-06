package com.ftec.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.repositories.UserDAO;
import com.ftec.services.Implementations.UserServiceImpl;
import com.ftec.utils.EntityGenerator;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    UserDAO userDAO;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserServiceImpl userService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createValidUser() throws Exception {
        RegistrationController.UserRegistration userRegistration = EntityGenerator.getNewRegisrtUser();

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is(200));

        assertTrue(userDAO.findByUsername(userRegistration.getUsername()).isPresent());
    }

    @Test
    public void trySaveDuplicateUsername() throws Exception {
        RegistrationController.UserRegistration userRegistration = EntityGenerator.getNewRegisrtUser();
        String userName = userRegistration.getUsername();

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(objectMapper.writeValueAsString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is(200));

        //should be status BadRequest
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(objectMapper.writeValueAsString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());

        assertTrue(userService.isDuplicateUserName(userName));
    }


    @Test
    public void registerTwoValidUsers() throws Exception {

        RegistrationController.UserRegistration userRegistration1 = EntityGenerator.getNewRegisrtUser();

        RegistrationController.UserRegistration userRegistration2 = EntityGenerator.getNewRegisrtUser();

        assertFalse(userDAO.findByUsername(userRegistration1.getUsername()).isPresent());
        assertFalse(userDAO.findByUsername(userRegistration2.getUsername()).isPresent());

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(objectMapper.writeValueAsString(userRegistration1)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(objectMapper.writeValueAsString(userRegistration2)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        assertTrue(userDAO.findByUsername(userRegistration1.getUsername()).isPresent());
        assertTrue(userDAO.findByUsername(userRegistration2.getUsername()).isPresent());

    }

}
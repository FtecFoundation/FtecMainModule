package com.ftec.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.repositories.UserDAO;
import com.ftec.services.Implementations.UserServiceImpl;
import com.ftec.services.TokenService;
import com.ftec.utils.EntityGenerator;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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

    @Test
    public void createValidUser() throws Exception {
        RegistrationController.UserRegistration userRegistration = EntityGenerator.getNewRegisrtUser();

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(asJsonString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is(200));

        assertTrue(userDAO.findByUsername(userRegistration.getUsername()).isPresent());
    }

    @Test(expected = NestedServletException.class)
    public void trySaveDuplicateUsername() throws Exception {
        RegistrationController.UserRegistration userRegistration = EntityGenerator.getNewRegisrtUser();
        String userName = userRegistration.getUsername();

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(asJsonString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is(200));

        //should be status BadRequest
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(asJsonString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());

        assertTrue(userService.isDuplicateUserName(userName));
    }


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkReturnedToken() throws Exception {
        RegistrationController.UserRegistration userRegistration = EntityGenerator.getNewRegisrtUser();

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(asJsonString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().exists(TokenService.TOKEN_NAME));
    }

    @Test
    public void registerTwoValidUsers() throws Exception {

        RegistrationController.UserRegistration userRegistration1 = EntityGenerator.getNewRegisrtUser();

        RegistrationController.UserRegistration userRegistration2 = EntityGenerator.getNewRegisrtUser();

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(asJsonString(userRegistration1)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200)).andExpect(header().exists(TokenService.TOKEN_NAME));

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
                content(asJsonString(userRegistration2)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200)).andExpect(header().exists(TokenService.TOKEN_NAME));

    }

}
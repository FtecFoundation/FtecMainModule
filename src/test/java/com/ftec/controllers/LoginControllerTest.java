package com.ftec.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.TokenDAO;
import com.ftec.services.Implementations.UserServiceImpl;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.EntityGenerator;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    UserDAO userDAO;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    TokenDAO tokenDAO;

    @Autowired
    TokenService tokenService;

    @Autowired
    RegistrationService registrationService;


    @Test
    public void authorization() throws Exception {

        User u = EntityGenerator.getNewUser();
        String username = u.getUsername();
        String pass = u.getPassword();
        String email = u.getEmail();

        u.setTwoStepVerification(false);

        registrationService.registerNewUserAccount(u);

        assertEquals(userDAO.findByUsername(username).get().getEmail(), email);

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("username", username)
                .param("password", pass)).andExpect(status().is(200));

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/login")
                .param("username", username)
                .param("password", "invalidPass")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isForbidden()).andReturn();

        MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/login")
                .param("username", "invalidLog")
                .param("password", pass)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isForbidden()).andReturn();

        assertEquals(new JSONObject(mvcResult1.getResponse().getContentAsString()).getString("error"), LoginController.INVALID_USERNAME_OR_PASSWORD);

        assertEquals(new JSONObject(mvcResult2.getResponse().getContentAsString()).getString("error"), LoginController.INVALID_USERNAME_OR_PASSWORD);
    }

    @Test
    public void authWithout2FaCode() throws Exception {

        User user = EntityGenerator.getNewUser();
        String username = user.getUsername();
        String pass = user.getPassword();
        user.setTwoStepVerification(true);

        registrationService.registerNewUserAccount(user);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/login")
                .param("username", username)
                .param("password", pass)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isForbidden()).andReturn();

        assertEquals(new JSONObject(mvcResult.getResponse().getContentAsString()).getString("error"), LoginController.EMPTY_2FA_CODE_MESSAGE);
    }

    @Test
    public void authWith2FaCode() throws Exception {
        User user = EntityGenerator.getNewUser();
        String userName = user.getUsername();
        String pass = user.getPassword();
        user.setTwoStepVerification(true);

        registrationService.registerNewUserAccount(user);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/login")
                .param("username", userName)
                .param("password", pass)
                .param("code", "with_test_pofile_its_ok")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();


    }
}
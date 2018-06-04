package com.ftec.controllers;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.UserTokenDAO;
import com.ftec.services.Implementations.UserServiceImpl;
import com.ftec.services.TokenService;
import com.ftec.utils.EntityGenerator;
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

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class AuthorizationControllerTest {

    @Autowired
    UserDAO userDAO;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserTokenDAO tokenDAO;

    @Autowired
    TokenService tokenService;

    @Before
    public void setUp(){
        userDAO.deleteAll();
        tokenDAO.deleteAll();
    }

    @Test
    public void authorization() throws Exception {
        User u = EntityGenerator.getNewUser();
        String username = u.getUsername();
        String pass = u.getPassword();
        u.setTwoStepVerification(false);

        userDAO.save(u);

        long id = u.getId();

        assertTrue(userDAO.findById(id).get().getUsername().equals(username));

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", username)
                .param("password", pass)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted());

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", "invaliUsername")
                .param("password", "pass")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", "log")
                .param("password", "invalidPass")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        assertTrue(mvcResult1.getResponse().getContentAsString()
                .equals(AuthorizationController.INVALID_USERNAME_OR_PASSWORD));

        assertTrue(mvcResult2.getResponse().getContentAsString()
                .equals(AuthorizationController.INVALID_USERNAME_OR_PASSWORD));
    }

    @Test
    public void authWithout2FaCode() throws Exception {

        User user = EntityGenerator.getNewUser();
        String username = user.getUsername();
        String pass = user.getPassword();
        user.setTwoStepVerification(true);

        userDAO.save(user);
        long id = user.getId();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", username)
                .param("password", pass)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        mvcResult.getResponse().getContentAsString().equals(AuthorizationController.EMPTY_2FA_CODE_MESSAGE);
    }

    @Test
    public void authWith2FaCode() throws Exception {
        User user = EntityGenerator.getNewUser();
        String userName = user.getUsername();
        String pass = user.getPassword();
        user.setTwoStepVerification(true);

        userDAO.save(user);
        long id = user.getId();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", userName)
                .param("password", pass)
                .param("twoStepVerCode", "with_test_pofile_its_ok")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted()).andReturn();

        String token = mvcResult.getResponse().getHeader(TokenService.TOKEN_NAME);

        assertTrue(tokenDAO.findByToken(token) != null);

        tokenService.verifyToken(token); //should not throw exception

    }
}

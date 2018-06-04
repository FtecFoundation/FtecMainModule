package com.ftec.controllers;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.UserToken;
import com.ftec.repositories.UserTokenDAO;
import com.ftec.services.TokenService;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class LogOutTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	UserTokenDAO tokenDAO;

	@Autowired
	TokenService tokenService;

	@Autowired
	LogOutController logOutController;

	@Test
	public void logOutTest() throws Exception {
        tokenService.createSaveAndGetNewToken(322L);
        String token = tokenService.createSaveAndGetNewToken(112L);
        Optional<UserToken> tokenFromDB = tokenDAO.findByToken(token);
        assert tokenFromDB.isPresent();

        mvc.perform(MockMvcRequestBuilders.get("/logout")
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted());

        Optional<UserToken> tokenFromDBAfterLogout = tokenDAO.findByToken(token);
    }
}
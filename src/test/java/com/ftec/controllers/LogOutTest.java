package com.ftec.controllers;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.entities.Token;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.TokenDAO;
import com.ftec.services.TokenService;
import com.ftec.utils.EntityGenerator;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class LogOutTest {

	@Autowired
	MockMvc mvc;

	@Autowired
    TokenDAO tokenDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	TokenService tokenService;

	@Autowired
	LogOutController logOutController;


	@Test
	public void logOutTest() throws Exception {
		User u = EntityGenerator.getNewUser();
		userDAO.save(u);

		String token = tokenService.createSaveAndGetNewToken(u.getId());

		assertTrue(tokenDAO.findByToken(token).isPresent());
		
		  mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/logout")
	                .header(TokenService.TOKEN_NAME, token)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk());

		assertFalse (tokenDAO.findByToken(token).isPresent());
	}
	
	@Test
	public void deleteTokenFromDB() {
		String token = tokenService.createSaveAndGetNewToken(EntityGenerator.getNextNum());
		Token tokenFromDB = tokenDAO.findByToken(token).get();
		
		assertNotNull(tokenFromDB);
		
		tokenDAO.deleteByToken(token);
		
		assertFalse(tokenDAO.findByToken(token).isPresent());
	}

}
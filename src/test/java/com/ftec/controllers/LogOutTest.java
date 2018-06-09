package com.ftec.controllers;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Token;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.TokenService;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class LogOutTest {

	@Autowired
	MockMvc mvc;

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

		assertTrue(tokenService.findByToken(token).isPresent());
		
		  mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/logout")
	                .header(TokenService.TOKEN_NAME, token)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk());

		assertFalse (tokenService.findByToken(token).isPresent());
	}
	
	@Test
	public void deleteTokenFromDB() {
		String token = tokenService.createSaveAndGetNewToken(EntityGenerator.getNextNum());
		Token tokenFromDB = tokenService.findByToken(token).get();
		
		assertNotNull(tokenFromDB);
		
		tokenService.deleteByToken(token);
		
		assertFalse(tokenService.findByToken(token).isPresent());
	}

}
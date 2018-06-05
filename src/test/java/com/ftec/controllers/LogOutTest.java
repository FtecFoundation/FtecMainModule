package com.ftec.controllers;

import com.ftec.configs.ApplicationConfig;
import com.ftec.configs.enums.TutorialSteps;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ftec.entities.User;
import com.ftec.entities.UserToken;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.UserTokenDAO;
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
	UserDAO userDAO;

	@Autowired
	TokenService tokenService;

	@Autowired
	LogOutController logOutController;

	@Before
	public void setUp() {
		userDAO.deleteAll();
		tokenDAO.deleteAll();
	}

	@Test
	public void logOutTest() throws Exception {
		User u = EntityGenerator.getNewUser();
		userDAO.save(u);

		String token = tokenService.createSaveAndGetNewToken(u.getId());
		UserToken tokenFromDB = tokenDAO.findByIdToken(token).get();
		assertNotNull(tokenFromDB);
		
		  mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/logout")
	                .header(TokenService.TOKEN_NAME, token)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isAccepted());

		  assertFalse(tokenDAO.findByIdToken(token).isPresent());
	}
	
	@Test
	public void testDelete() {
		String token = tokenService.createSaveAndGetNewToken(EntityGenerator.getNextNum());
		UserToken tokenFromDB = tokenDAO.findByIdToken(token).get();
		
		assertNotNull(tokenFromDB);
		
		tokenDAO.delete(tokenFromDB);
		
		assertFalse(tokenDAO.findByIdToken(token).isPresent());
	}

}
package com.ftec.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.resources.enums.TutorialSteps;
import com.ftec.controllers.ChangeSettingController.UserUpdate;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
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

import java.util.Iterator;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ChangeUserDataTest {

	@Autowired
	UserDAO userDAO;

	@Autowired
	MockMvc mvc;
	
	@Autowired
	TokenService service;
	
	ObjectMapper objectMapper = new ObjectMapper();

	String username = "user1";
	String password = "pass1";
	String email = "email@d.net";
	TutorialSteps currentStep = TutorialSteps.FIRST;
	boolean subscribeForNews = false;
	Boolean twoStepVerification = false;

	@Test
	public void changeUserDataTest() throws Exception {
		User u =  EntityGenerator.getNewUser();

		u.setTwoStepVerification(false);
		userDAO.save(u);

		long id = u.getId();

		UserUpdate updatedUserData = new UserUpdate();
		updatedUserData.setPassword("neWStrong123");
		updatedUserData.setEmail("new_email@gmail.com");
		updatedUserData.setTwoFactorEnabled(true);
		
		String token = service.createSaveAndGetNewToken(id);
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( objectMapper.writeValueAsString(updatedUserData)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isOk());
		
		User userAfterChange = userDAO.findById(id).get();
		
		assert userAfterChange.getPassword().equals("neWStrong123");
		assert userAfterChange.getEmail().equals("new_email@gmail.com");
		assert userAfterChange.getTwoStepVerification();
		}
	
	@Test
	public void changeNothing() throws Exception {
		User u = EntityGenerator.getNewUser();

		userDAO.save(u);
		String token = service.createSaveAndGetNewToken(u.getId());

		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( objectMapper.writeValueAsString(new UserUpdate())).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isOk());

	}
	
	@Test
	public void tryChangeToInvalidEmailAndPass() throws Exception {

		User u = EntityGenerator.getNewUser();

		u.setEmail("validEmail123@gmail.com");
		userDAO.save(u);

		String token = service.createSaveAndGetNewToken(u.getId());

		UserUpdate userUpdate = new UserUpdate();
		userUpdate.setEmail("invalidEmail");
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( objectMapper.writeValueAsString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isBadRequest());
		
		userUpdate.setEmail("validEmail@Gmail.com");
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( objectMapper.writeValueAsString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isOk());
		
		userUpdate.setPassword("invalidpass");
		userUpdate.setEmail(null);

		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( objectMapper.writeValueAsString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isBadRequest());
		
		userUpdate.setPassword("validPass1231");
		userUpdate.setEmail(null);

		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( objectMapper.writeValueAsString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
				.andDo(print()).andExpect(status().isOk());

		userDAO.deleteAll();
	}

	@Test
	public void trySaveDublicateEmail() throws Exception {
		User u =  EntityGenerator.getNewUser();
		u.setEmail("dublicate_email@wda.net");
		userDAO.save(u);

		User u2 = EntityGenerator.getNewUser();
		u2.setEmail("ok_email@gmail.com");
		userDAO.save(u2);

		String token = service.createSaveAndGetNewToken(u2.getId());

		UserUpdate userUpdate = new UserUpdate();

		userUpdate.setEmail("dublicate_email@wda.net");

		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( objectMapper.writeValueAsString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
				.andDo(print()).andExpect(status().isBadRequest());

		userDAO.deleteAll();
	}
}

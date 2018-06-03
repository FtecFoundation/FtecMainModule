package com.ftec.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Iterator;

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

import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.ChangeSettingController.UserUpdate;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.services.TokenService;

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
	
	@Before
	public void SetUp() {
		userDAO.deleteAll();
	}
	
	private void printUser() {
		Iterable<User> allIteration = userDAO.findAll();
		Iterator<User> iterator = allIteration.iterator();
		for (User user : allIteration) {
			System.out.println(user);
		}
	}
	
	@Test
	public void changeUserDataIntegrationTest() throws Exception {
		User u = new User();
		long id = 88L;
		u.setId(id);
		u.setPassword("old_password");
		u.setEmail("old_email");
		u.setTwoStepVerification(false);
		userDAO.save(u);
		
		UserUpdate updatedUserData = new UserUpdate();
		updatedUserData.setPassword("neWStrong123");
		updatedUserData.setEmail("new_email@gmail.com");
		updatedUserData.setTwoFactorEnabled(true);
		
		String token = service.createSaveAndGetNewToken(id);
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(updatedUserData)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isOk());
		
		User userAfterChange = userDAO.findById(id).get();
		
		assert userAfterChange.getPassword().equals("neWStrong123");
		assert userAfterChange.getEmail().equals("new_email@gmail.com");
		assert userAfterChange.isTwoStepVerification() == true;
	}
	
	@Test
	public void changeNothing() throws Exception {
		String token = service.createSaveAndGetNewToken(295L);
		User u = new User();
		u.setId(295L);
		userDAO.save(u);
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(new UserUpdate())).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void tryChangeToInvalidData() throws Exception {
		String token = service.createSaveAndGetNewToken(2911L);
		User u = new User();
		u.setId(2911L);
		u.setEmail("email_1");
		userDAO.save(u);
		
		UserUpdate userUpdate = new UserUpdate();
		userUpdate.setEmail("invalidEmail");
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isBadRequest());
		
		userUpdate.setEmail("validEmail@Gmail.com");
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isOk());
		
		userUpdate.setPassword("invalidpass");
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isBadRequest());
		
		User u2 = new User();
		u2.setId(11L);
		u2.setEmail("dublicate_email@wda.net");
		userDAO.save(u2);
		
		userUpdate.setEmail("dublicate_email@wda.net");
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isBadRequest());
	}
}

package com.ftec.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Iterator;

import com.ftec.configs.enums.TutorialSteps;
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

	String username = "user1";
	String password = "pass1";
	String email = "email@d.net";
	TutorialSteps currentStep = TutorialSteps.FIRST;
	boolean subscribeForNews = false;
	Boolean twoStepVerification = false;

	@Test
	public void changeUserDataIntegrationTest() throws Exception {
		User u =  new User(username, password,
				email, currentStep, subscribeForNews, twoStepVerification);

		u.setPassword("old_passworD123");
		u.setEmail("old_email");
		u.setTwoStepVerification(false);
		userDAO.save(u);

		long id = u.getId();

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

		userDAO.deleteAll();
	}
	
	@Test
	public void changeNothing() throws Exception {
		User u = new User(username, password,
				email, currentStep, subscribeForNews, twoStepVerification);
		userDAO.save(u);
		long id = u.getId();

		String token = service.createSaveAndGetNewToken(id);

		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(new UserUpdate())).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isOk());

		userDAO.deleteAll();
	}
	
	@Test
	public void tryChangeToInvalidEmailAndPass() throws Exception {


		User u = new User(username, password,
				email, currentStep, subscribeForNews, twoStepVerification);

		u.setEmail("validEmail123@gmail.com");
		userDAO.save(u);

		String token = service.createSaveAndGetNewToken(u.getId());

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
		userUpdate.setEmail(null);

		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
		.andDo(print()).andExpect(status().isBadRequest());
		
		userUpdate.setPassword("validPass1231");
		userUpdate.setEmail(null);

		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
				.andDo(print()).andExpect(status().isOk());

		userDAO.deleteAll();
	}

	@Test
	public void trySaveDublicateEmail() throws Exception {
		User u = new User(username, password,
				email, currentStep, subscribeForNews, twoStepVerification);

		u.setEmail("dublicate_email@wda.net");
		userDAO.save(u);

		User u2 = new User(username, password,
				email, currentStep, subscribeForNews, twoStepVerification);

		u2.setEmail("goodEmail@gmail.com");
		String token = service.createSaveAndGetNewToken(u2.getId());
		userDAO.save(u2);

		UserUpdate userUpdate = new UserUpdate();

		userUpdate.setEmail("dublicate_email@wda.net");

		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/changeUserSetting")
				.content( ControllerTest.asJsonString(userUpdate)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenService.TOKEN_NAME, token))
				.andDo(print()).andExpect(status().isBadRequest());

		userDAO.deleteAll();
	}
}

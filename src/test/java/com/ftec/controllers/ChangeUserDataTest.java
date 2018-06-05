package com.ftec.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.ChangeSettingController.UserUpdate;
import com.ftec.entities.User;
import com.ftec.exceptions.UserNotExistsException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.ChangeSettingsService;
import com.ftec.services.interfaces.RegistrationService;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ChangeUserDataTest {

	@Autowired
	RegistrationService registrationService;
	
	@Autowired
	ChangeSettingsService changeSettingsService;

	@Autowired
	TokenService tokenService;

	@Autowired
	MockMvc mvc;

	@Autowired
	UserDAO userDAO;

	private ObjectMapper objectMapper = new ObjectMapper();


	@Test
	public void changeUserDataIntegrationTest() throws Exception, UserNotExistsException {
		User u =  EntityGenerator.getNewUser();
		registrationService.registerNewUserAccount(u);

		long id = u.getId();

		UserUpdate updatedUserData = new UserUpdate();
		updatedUserData.setPassword("neWStrong123");
		updatedUserData.setEmail("new_email@gmail.com");

		changeSettingsService.updatePreferences(updatedUserData, id);
		Optional<User> user = userDAO.findById(id);
		if(!user.isPresent()) throw new NullPointerException();
		User userAfterChange = user.get();
		
		assert userAfterChange.getPassword().equals("neWStrong123");
		assert userAfterChange.getEmail().equals("new_email@gmail.com");
		assert !userAfterChange.getTwoStepVerification();
	}

	@Test
	public void changeNothing() throws Exception, UserNotExistsException {
		User u = EntityGenerator.getNewUser();
		registrationService.registerNewUserAccount(u);

		changeSettingsService.updatePreferences(new UserUpdate(), u.getId());

		Optional<User> user = userDAO.findById(u.getId());
		if(!user.isPresent()) throw new NullPointerException();
		User userAfterChange = user.get();

		assert userAfterChange.getPassword().equals(u.getPassword());
		assert userAfterChange.getEmail().equals(u.getEmail());
		assert !userAfterChange.getTwoStepVerification();
	}
	
	@Test
	public void tryChangeToInvalidEmailAndPass() throws Exception {

		User u = EntityGenerator.getNewUser();

		u.setEmail("validEmail123@gmail.com");
		userDAO.save(u);

		String token = tokenService.createSaveAndGetNewToken(u.getId());

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

		String token = tokenService.createSaveAndGetNewToken(u2.getId());

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

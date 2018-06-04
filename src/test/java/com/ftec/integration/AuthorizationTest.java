package com.ftec.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.AuthorizationController;
import com.ftec.controllers.RegistrationController;
import com.ftec.repositories.UserTokenDAO;
import com.ftec.services.Implementations.UserServiceImpl;
import com.ftec.services.RegistrationService;
import com.ftec.services.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class AuthorizationTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	RegistrationService registrationService;

	@Autowired
	UserTokenDAO tokenDAO;

	@Autowired
	TokenService tokenService;

	@Test
	public void authorization() throws Exception {
		String login = "log";
		String pass = "pass";
		String validEmail = "example@example.com";
		String invalidLogin = "invalid login";
		String invalidPassword = "invalid password";
		ObjectMapper objectMapper = new ObjectMapper();

		RegistrationController.UserRegistration user = new RegistrationController.UserRegistration(login, pass, validEmail, true);

		MvcResult result = mvc.perform(post("/registration").
				content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(200)).andReturn();
		String token = new JSONObject(result.getResponse().getContentAsString()).getString("token");
		assert !token.isEmpty();

		mvc.perform(post("http://localhost:8080/authorization")
				.param("username", login)
				.param("password", pass)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isAccepted());

		MvcResult mvcResult1 = mvc.perform(post("http://localhost:8080/authorization")
				.param("username", invalidLogin)
				.param("password", pass)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		MvcResult mvcResult2 = mvc.perform(post("http://localhost:8080/authorization")
				.param("username", login)
				.param("password", invalidPassword)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		assertEquals(mvcResult1.getResponse().getContentAsString(), AuthorizationController.INVALID_USERNAME_OR_PASSWORD);
		assertEquals(mvcResult2.getResponse().getContentAsString(), AuthorizationController.INVALID_USERNAME_OR_PASSWORD);
	}

//	@Test
//	public void authWithout2FaCode() throws Exception {
//		String userName = "log1";
//		User user = ControllerTest.newUser(userName);
//		Long id = 132L;
//		user.setId(id);
//		String pass = "pass";
//		user.setPassword(pass);
//		user.setTwoStepVerification(true);
//		userDAO.save(user);
//
//
//		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
//				.param("username", "log1")
//				.param("password", "pass")
//				.contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON))
//				.andDo(print()).andExpect(status().isBadRequest()).andReturn();
//
//		assertEquals(mvcResult.getResponse().getContentAsString(),AuthorizationController.EMPTY_2FA_CODE_MESSAGE);
//	}
//
//	@Test
//	public void authWith2FaCode() throws Exception {
//		String userName = "log1";
//		User user = ControllerTest.newUser(userName);
//		Long id = 132L;
//		user.setId(id);
//		String pass = "pass";
//		user.setPassword(pass);
//		user.setTwoStepVerification(true);
//		userDAO.save(user);
//
//		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
//				.param("username", "log1")
//				.param("password", "pass")
//				.param("twoStepVerCode", "with_test_pofile_its_ok")
//				.contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON))
//				.andDo(print()).andExpect(status().isAccepted()).andReturn();
//
//		String token = mvcResult.getResponse().getHeader(TokenService.TOKEN_NAME);
//
//		assertNotNull(tokenDAO.findByToken(token));
//
//		tokenService.verifyToken(token); //should not throw exception
//
//	}
}
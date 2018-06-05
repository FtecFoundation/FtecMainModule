package com.ftec.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.configs.enums.TutorialSteps;
import com.ftec.controllers.ChangeSettingController;
import com.ftec.controllers.RegistrationController;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.UserTokenDAO;
import com.ftec.services.Implementations.UserServiceImpl;
import com.ftec.services.RegistrationService;
import com.ftec.services.TokenService;
import com.ftec.utils.EntityGenerator;
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

	@Autowired
    UserDAO userDAO;

	@Test
	public void authorization() throws Exception {
		String invalidLogin = "invalid login";
		String invalidPassword = "invalid password";
		ObjectMapper objectMapper = new ObjectMapper();

		RegistrationController.UserRegistration user = EntityGenerator.getNewRegisrtUser();

        String username = user.getUsername();
        String password = user.getPassword();

        assertFalse(userDAO.findByUsername(user.getUsername()).isPresent());
		MvcResult result = mvc.perform(post("/registration")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(200)).andReturn();
        assertTrue(userDAO.findByUsername(user.getUsername()).isPresent());

		String token = new JSONObject(result.getResponse().getContentAsString()).getJSONObject("response").getString("token");
		assert !token.isEmpty();

		TutorialSteps step = TutorialSteps.valueOf(mvc.perform(get("/cabinet/tutorial/getCurrentStep")
				.header(TokenService.TOKEN_NAME,token)
		).andExpect(status().is(200)).andReturn().getResponse().getContentAsString().replaceAll("^\"|\"$", ""));

		assertEquals(TutorialSteps.FIRST, step);

		String logoutResult = mvc.perform(post("/logout").header(TokenService.TOKEN_NAME,token))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString();

		//with removed token
        mvc.perform(post("/changeUserSetting")
                .header(TokenService.TOKEN_NAME,token)
        ).andExpect(status().is(403));

        //with invalid token
        mvc.perform(post("/changeUserSetting")
                .header(TokenService.TOKEN_NAME,"23_DWWDAAWDDWA")
        ).andExpect(status().is(403));

        //without token
        mvc.perform(post("/changeUserSetting")
        ).andExpect(status().is(403));

		assertEquals("ok", logoutResult);
        assertFalse(tokenService.findByToken(token).isPresent());

		String invalidCredentialsAnswer = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
				.param("username",invalidLogin)
				.param("password", invalidPassword)).andExpect(status().is(403)).andReturn().getResponse().getContentAsString();

        JSONObject mvcResponseString = new JSONObject(invalidCredentialsAnswer);

        assertEquals(mvcResponseString.getInt("status"),403);

        MvcResult resultLogin = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("username", username)
                .param("password", password)).andExpect(status().is(200)).andReturn();

        String tokenAfterLogin = new JSONObject(resultLogin.getResponse().getContentAsString()).getJSONObject("response").getString("token");


        assertFalse(userDAO.findByUsername(user.getUsername()).get().getTwoStepVerification());

        ChangeSettingController.UserUpdate updateSetting = new ChangeSettingController.UserUpdate();
        updateSetting.setTwoFactorEnabled(true);


        mvc.perform(post("/changeUserSetting")
                .header(TokenService.TOKEN_NAME,tokenAfterLogin)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateSetting))
        ).andExpect(status().is(200));

        assertTrue(userDAO.findByUsername(user.getUsername()).get().getTwoStepVerification());

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
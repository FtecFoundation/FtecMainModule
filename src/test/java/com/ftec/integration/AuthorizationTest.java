package com.ftec.integration;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedHashMap;
import java.util.Map;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.AuthorizationController;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.UserTokenDAO;
import com.ftec.services.TokenService;
import com.ftec.services.Implementations.UserServiceImpl;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class AuthorizationTest {
	
	@Autowired
	UserDAO userDAO;

	@Autowired
	MockMvc mvc;
	
	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	UserTokenDAO tokenDAO;
	
	@Autowired
	TokenService tokenService;


	@Before
	public void setUp() {
		userDAO.deleteAll();
		tokenDAO.deleteAll();
	}
	
	public static User newUser(String login) {
		User u = new User();
		u.setUsername(login);
		u.setPassword("pass_user1");
		u.setEmail("emaill");
		
		return u;
	}
	
	@Test
	public void authorization() throws Exception {
		String login = "log";
		User u = newUser(login);
		u.setUsername(login);
		String pass = "pass";
		u.setPassword(pass);
		u.setTwoStepVerification(false);
		
		userDAO.save(u);
		long id = u.getId();
		Map<String,String> map = new LinkedHashMap<>();
		map.put("username", "log");
		map.put("password", pass);
		
		assertTrue(userDAO.findById(id).get().getUsername().equals(login));
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", "log")
                .param("password", "pass")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted()).andReturn();
		
		MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", "invaliUsername")
                .param("password", "pass")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
		
		MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", "log")
                .param("password", "invalidPass")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

		assertTrue(mvcResult1.getResponse().getContentAsString()
				.equals(AuthorizationController.INVALID_USERNAME_OR_PASSWORD));
		
		assertTrue(mvcResult2.getResponse().getContentAsString()
				.equals(AuthorizationController.INVALID_USERNAME_OR_PASSWORD));
	}
	
	@Test
	public void authoWithout2FaCode() throws Exception {
		String userName = "log1";
		User user = newUser(userName);
		String pass = "pass";
		user.setPassword(pass);
		user.setTwoStepVerification(true);
		userDAO.save(user);
		long id = user.getId();
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", "log1")
                .param("password", "pass")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn(); 
		
		mvcResult.getResponse().getContentAsString().equals(AuthorizationController.EMPTY_2FA_CODE_MESSAGE);
	}
	
	@Test
	public void authWith2FaCode() throws Exception {
		String userName = "log1";
		User user = newUser(userName);
		String pass = "pass";
		user.setPassword(pass);
		user.setTwoStepVerification(true);
		userDAO.save(user);
		long id = user.getId();

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/authorization")
                .param("username", "log1")
                .param("password", "pass")
                .param("twoStepVerCode", "with_test_pofile_its_ok")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted()).andReturn(); 
		
		String token = mvcResult.getResponse().getHeader(TokenService.TOKEN_NAME);
		
		assertTrue(tokenDAO.findByToken(token) != null);
		
		tokenService.verifyToken(token); //should not throw exception
		
	}
}

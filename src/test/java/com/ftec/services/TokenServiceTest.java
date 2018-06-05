package com.ftec.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.ControllerTest;
import com.ftec.entities.User;
import com.ftec.entities.Token;
import com.ftec.exceptions.token.InvalidTokenException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.TokenDAO;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class TokenServiceTest {
    @Autowired
    public TokenService tokenService;

    @Autowired
    public TokenDAO tokenDAO;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserDAO dao;

    @Autowired
    UserDAO userDao;

    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    public void getValidIdFromTokenTest() {
        assertThat(TokenService.extractUserID("23_NDKJAWNWKAJDNAkWKDNAW"),is("23"));
    }

    @Test
    public void tokenFormatTest() {
        Long testId = 199L;
        String generatedToken = TokenService.generateToken(testId);

        assertTrue(generatedToken.startsWith("199_"));

        String userId = generatedToken.substring(0, generatedToken.indexOf("_"));

        assertThat(Long.valueOf(userId), is(testId));
    }

    @Test(expected = InvalidTokenException.class)
    public void firstTestExceptionWhileInvalidTokenFormat() {
        TokenService.checkTokenFormat("23a_NDKJAWNWKAJDNAkWKDNAW");
    }

    @Test(expected = InvalidTokenException.class)
    public void secondTestExceptionWhileInvalidTokenFormat() {
        TokenService.checkTokenFormat("23aNDKJAWNWKAJDNAkWKDNAW");
    }

    @Test
    public void testSaveTokenAndUserIntoDBthroughRegistrationController() throws Exception {
        User u = EntityGenerator.getNewUser();
        String userName = u.getUsername();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration").
				content( objectMapper.writeValueAsString(u)).contentType(MediaType.APPLICATION_JSON).
				accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk()).andReturn();

		String mvcResponse = mvcResult.getResponse().getContentAsString();
        String token = new JSONObject(mvcResponse).getJSONObject("response").getString("token");

        Long id = Long.valueOf(TokenService.extractUserID(token));

        assertNotNull(tokenDAO.findByToken(token));

		assertEquals(userDao.findById(id).get().getUsername(),userName);

		userDao.deleteById(id);
		assertFalse(userDao.findById(id).isPresent());
    }

    @Test
    public void saveTokenIntoDB() {
    	String token = TokenService.generateToken(998L);
    	Date current = new Date();
    	Token uToken = new Token(token, current);
    	
    	tokenDAO.save(uToken);

        assertNotNull(tokenDAO.findByToken(token));

        assertEquals(tokenDAO.findByToken(token).get().getToken(), token);
    }

    @Test(expected = TokenException.class)
    public void testExpiration() throws InterruptedException {
        Date expired = new Date();
        Thread.sleep(500);
        System.out.println("expired date = " + expired);
        TokenService.checkIfTokenExpired(expired);//should throw an exception
    }
}

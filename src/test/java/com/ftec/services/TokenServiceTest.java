package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.ControllerTest;
import com.ftec.entities.User;
import com.ftec.entities.UserToken;
import com.ftec.exceptions.token.InvalidTokenException;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.UserTokenDAO;
import org.junit.Assert;
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

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class TokenServiceTest {
    @Autowired
    public TokenService tokenService;

    @Autowired
    public UserTokenDAO tokenDAO;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserDAO dao;

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

    @Test //integration test
    public void testSaveTokenIntoDBthroughRegistrationController() throws Exception {
        String userName = "tester2";
        User u = ControllerTest.newUser(userName);
        Long id = 565L;
        u.setId(id);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration/registr_test").
                content( ControllerTest.asJsonString(u)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andExpect(header().exists(TokenService.TOKEN_NAME)).andReturn();

        String token = mvcResult.getResponse().getHeader(TokenService.TOKEN_NAME);

        assertNotNull(tokenDAO.findByToken(token));

        dao.deleteById(id);

    }

    @Test
    public void saveTokenIntoDB() {
        String token = TokenService.generateToken(998L);
        Date current = new Date();
        UserToken uToken = new UserToken(token, current);

        tokenDAO.save(uToken);

        assertNotNull(tokenDAO.findByToken(token));
        Assert.assertEquals(tokenDAO.findByToken(token).get().getExpirationTime(), current);
    }
}

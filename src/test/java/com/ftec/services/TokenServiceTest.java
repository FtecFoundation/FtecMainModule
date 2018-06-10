package com.ftec.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.ChangeSettingController;
import com.ftec.entities.Token;
import com.ftec.entities.User;
import com.ftec.exceptions.token.InvalidTokenException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.services.Implementations.TokenServiceImpl;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.TokenService;
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
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class TokenServiceTest {
    @Autowired
    public TokenService tokenService;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserDAO dao;

    @Autowired
    UserDAO userDao;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    TokenServiceImpl tokenServiceImpl;

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
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().isOk()).andReturn();

        String mvcResponse = mvcResult.getResponse().getContentAsString();
        String token = new JSONObject(mvcResponse).getJSONObject("response").getString("token");

        Long id = Long.valueOf(TokenService.extractUserID(token));

        assertNotNull(tokenService.findByToken(token));

        assertEquals(userDao.findById(id).get().getUsername(),userName);

        userDao.deleteById(id);
        assertFalse(userDao.findById(id).isPresent());
    }

    @Test
    public void saveTokenIntoDB() {
        String token = TokenService.generateToken(998L);
        Date current = new Date();
        Token uToken = new Token(token, current);

        tokenService.save(uToken);

        assertNotNull(tokenService.findByToken(token));

        assertEquals(tokenService.findByToken(token).get().getToken(), token);
    }

    @Test(expected = TokenException.class)
    public void testExpiration() throws InterruptedException {
        Date expired = new Date();
        Thread.sleep(500);
        System.out.println("expired date = " + expired);
        TokenService.checkIfTokenExpired(expired);//should throw an exception
    }

    @Test
    public void deleteAllByUserIdTest() {
       long userId = 228l;

       tokenService.createSaveAndGetNewToken(userId);
       tokenService.createSaveAndGetNewToken(userId);
       tokenService.createSaveAndGetNewToken(userId);
       tokenService.createSaveAndGetNewToken(userId);
       tokenService.createSaveAndGetNewToken(userId);
       tokenService.createSaveAndGetNewToken(userId);
       tokenService.createSaveAndGetNewToken(userId);

        tokenService.findAllByUserId(userId);
       assertEquals(7, tokenService.findAllByUserId(userId).size());

       tokenService.deleteExcessiveToken(userId);

       assertEquals(5, tokenService.findAllByUserId(userId).size());

    }

    @Test
    public void checkIfTokenExpirationTimeExtended() throws Exception {
        User u = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(u);

        String token = tokenService.createSaveAndGetNewToken(u.getId());
        Optional<Token> tokenFromDb = tokenService.findByToken(token);
        Date oldDate = tokenFromDb.get().getExpirationTime();

        Thread.sleep(1500);

        ChangeSettingController.UserUpdate nullChanges = new ChangeSettingController.UserUpdate();

        mvc.perform(post("/changeUserSetting")
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(nullChanges))
        ).andExpect(status().is(200));

        assertTrue(oldDate.before(tokenService.findByToken(token).get().getExpirationTime()));
    }

    @Test
    public void deleteAllExpired() throws InterruptedException {
        tokenService.deleteAll();
        assertEquals(0, tokenService.getAll().size());

        tokenService.createSaveAndGetNewToken(1L);
        tokenService.createSaveAndGetNewToken(1L);
        tokenService.createSaveAndGetNewToken(1L);

        assertEquals(3, tokenService.getAll().size());

        tokenServiceImpl.deleteExpiredTokens();
        assertEquals(3, tokenService.getAll().size());

        Token t1 = new Token("12_dwwad", new Date());
        Token t2 = new Token("11_wadwda", new Date());

        tokenService.save(t1);
        tokenService.save(t2);
        Thread.sleep(2000);

        assertEquals(5, tokenService.getAll().size());

        tokenServiceImpl.deleteExpiredTokens();

        //it can not works if database`s date is not configured!!
        assertEquals(3, tokenService.getAll().size());

    }

}
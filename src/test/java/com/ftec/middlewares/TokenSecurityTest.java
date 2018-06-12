
package com.ftec.middlewares;

import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.LogOutController;
import com.ftec.controllers.TutorialController;
import com.ftec.entities.Token;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.services.interfaces.TokenService;
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
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class TokenSecurityTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserDAO userDAO;

    @Test
    public void securityAccess() throws Exception {
        String token = tokenService.createSaveAndGetNewToken(EntityGenerator.getNextNum());
        assertTrue(tokenService.findByToken(token).isPresent());

        mvc.perform(MockMvcRequestBuilders.post(LogOutController.LOGOUT_URL)
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().isOk());

        tokenService.deleteByToken(token);
        assertFalse(tokenService.findByToken(token).isPresent());
    }

    @Test
    public void tryAccessWithoutValidToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(TutorialController.TUTORIAL_NEXT_STEP_URL)
                .header(TokenService.TOKEN_NAME, "123_UNVALIDTOKEN")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().isForbidden());
    }

    @Test
    public void tryAccessWithExpiredToken() throws Exception {
        User user = EntityGenerator.getNewUser();
        userDAO.save(user);

        long id = user.getId();

        Token token = new Token();
        token.setToken(id+"_"+"wdawdawdafa");
        Date normalTime = new Date();
        normalTime.setTime(new Date().getTime() + 180000);
        token.setExpirationTime(normalTime);
        tokenService.save(token);

        mvc.perform(MockMvcRequestBuilders.post(LogOutController.LOGOUT_URL)
                .header(TokenService.TOKEN_NAME, id+"_"+"wdawdawdafa")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().isOk());

        Token expiredToken = new Token();
        expiredToken.setToken(id+"_dwankdwakj");
        Date expiredDate = new Date();
        expiredDate.setTime(new Date().getTime() - 10000);
        expiredToken.setExpirationTime(expiredDate);
        tokenService.save(expiredToken);

        mvc.perform(MockMvcRequestBuilders.post(LogOutController.LOGOUT_URL)
                .header(TokenService.TOKEN_NAME, id+"_"+"dwankdwakj")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().isForbidden());
    }

}
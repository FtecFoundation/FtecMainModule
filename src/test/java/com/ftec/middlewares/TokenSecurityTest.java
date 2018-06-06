
package com.ftec.middlewares;
import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Token;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.TokenDAO;
import com.ftec.services.TokenService;
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

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class TokenSecurityTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TokenDAO tokenDAO;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserDAO userDAO;

    @Test
    public void securityAccess() throws Exception {
        String token = tokenService.createSaveAndGetNewToken(EntityGenerator.getNextNum());
        assertTrue(tokenDAO.findByToken(token).isPresent());

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/logout")
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        tokenDAO.deleteByToken(token);
        assertFalse(tokenDAO.findByToken(token).isPresent());
    }

    @Test
    public void tryAccessWithoutValidToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/cabinet/tutorial/nextStep")
                .header(TokenService.TOKEN_NAME, "123_UNVALIDTOKEN")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isForbidden());
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
        tokenDAO.save(token);

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/logout")
                .header(TokenService.TOKEN_NAME, id+"_"+"wdawdawdafa")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        Token expiredToken = new Token();
        expiredToken.setToken(id+"_dwankdwakj");
        Date expiredDate = new Date();
        expiredDate.setTime(new Date().getTime() - 10000);
        expiredToken.setExpirationTime(expiredDate);
        tokenDAO.save(expiredToken);

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/logout")
                .header(TokenService.TOKEN_NAME, id+"_"+"dwankdwakj")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isForbidden());
    }

}
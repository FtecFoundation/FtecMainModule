package com.ftec.repositories;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Token;
import com.ftec.services.TokenService;
import com.ftec.utils.EntityGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ActiveProfiles(value = "test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
public class TokenDBTest {

    @Autowired
    TokenDAO tokenDAO;

    @Autowired
    TokenService service;

    @Before
    public void setUp(){
        tokenDAO.deleteAll();
    }

    @Test
    public void TokenDBtest() {
        String token = service.createSaveAndGetNewToken(EntityGenerator.getNextNum());
        Optional<Token> byId = tokenDAO.findByToken(token);
        Token userToken = byId.get();

        assertTrue(tokenDAO.findByToken(token).get().getToken().equals(token));
        tokenDAO.deleteByToken(token);
        assertFalse(tokenDAO.findByToken(token).isPresent());
    }
}

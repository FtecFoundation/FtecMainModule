package com.ftec.repositories;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Token;
import com.ftec.services.interfaces.TokenService;
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

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
public class TokenDBTest {


    @Autowired
    TokenService tokenService;

    @Before
    public void setUp(){
        tokenService.deleteAll();
    }

    @Test
    public void TokenDBtest() {
        String token = tokenService.createSaveAndGetNewToken(EntityGenerator.getNextNum());
        Optional<Token> byId = tokenService.findByToken(token);
        Token userToken = byId.get();

        assertTrue(tokenService.findByToken(token).get().getToken().equals(token));
        tokenService.deleteByToken(token);
        assertFalse(tokenService.findByToken(token).isPresent());
    }
}

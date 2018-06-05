package com.ftec.repositories;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.UserToken;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
public class TokenDBTest {

    @Autowired
    UserTokenDAO tokenDAO;

    @Autowired
    TokenService service;

    @Before
    public void setUp(){
        tokenDAO.deleteAll();
    }

    @Test
    public void TokenDBtest() {
        String token = service.createSaveAndGetNewToken(EntityGenerator.getNextNum());
        Optional<UserToken> byId = tokenDAO.findByIdToken(token);
        UserToken userToken = byId.get();

        assertTrue(tokenDAO.findByIdToken(token).get().getToken().equals(token));
        tokenDAO.deleteByIdToken(token);
        assertFalse(tokenDAO.findByIdToken(token).isPresent());
    }
}

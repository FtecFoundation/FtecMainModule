package com.ftec.integration;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.repositories.RestoreDataDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.services.Implementations.RestoreDataServiceImpl;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.EntityGenerator;
import com.ftec.utils.PasswordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class RestorePassTest {

    @Autowired
    RestoreDataDAO restoreDataDAO;

    @Autowired
    RestoreDataServiceImpl restoreDataService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserDAO userDAO;

    @Autowired
    TokenService tokenService;

    @Test
    public void fullTest() throws Exception {
        User u = EntityGenerator.getNewUser();
        u.setEmail(Resources.sendToStatic);
        registrationService.registerNewUserAccount(u);

        assertFalse(restoreDataDAO.findById(u.getId()).isPresent());

        mvc.perform(post("/sendRestoreUrl")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("email", u.getEmail()))
                .andExpect(status().is(200));

        assertTrue(restoreDataDAO.findById(u.getId()).isPresent());

        String old_pass = u.getPassword();
        String hash = restoreDataDAO.findById(u.getId()).get().getHash();

        String new_clean_pass = "newStrongPass123";
        mvc.perform(post("/changePass")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("hash", hash)
                .param("new_pass", new_clean_pass))
                .andExpect(status().is(200));

        String new_pass = userDAO.findById(u.getId()).get().getPassword();

        assertNotEquals(old_pass,new_pass);
        assertEquals(new_pass,PasswordUtils.encodeUserPassword(new_clean_pass, userDAO.findById(u.getId()).get().getSalt()));
    }
}

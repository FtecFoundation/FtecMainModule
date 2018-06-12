package com.ftec.integration;

import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.RestoreController;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.resources.enums.ConfirmScope;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.ConfirmDataService;
import com.ftec.services.interfaces.TokenService;
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

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class RestorePassTest {

    @Autowired
    ConfirmDataService confirmDataService;

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
        u.setEmail(Resources.sendToStatic != null ? Resources.sendToStatic : "ndmawjkdnawjk@gmail.com");
        registrationService.registerNewUserAccount(u);

        assertFalse(confirmDataService.findByUserIdAndScope(u.getId(), ConfirmScope.RestorePass).isPresent());

        mvc.perform(post(RestoreController.SEND_RESTORE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("data", u.getEmail()))
                .andExpect(status().is(200));

        assertTrue(confirmDataService.findById(u.getId()).isPresent());

        String old_pass = u.getPassword();
        String hash = confirmDataService.findById(u.getId()).get().getHash();

        String new_clean_pass = "newStrongPass123";
        mvc.perform(post("/changePass")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("hash", hash)
                .param("new_pass", new_clean_pass))
                .andExpect(status().is(200));

        assertFalse(confirmDataService.findByUserIdAndScope(u.getId(), ConfirmScope.RestorePass).isPresent()); // after changed pass hash should be deleted

        String new_pass = userDAO.findById(u.getId()).get().getPassword();

        assertNotEquals(old_pass,new_pass);
        assertEquals(new_pass,PasswordUtils.encodeUserPassword(new_clean_pass, userDAO.findById(u.getId()).get().getSalt()));
    }
}
package com.ftec.constratints;


import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.resources.Resources;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ConstraintsController {

    @Autowired
    MockMvc mvc;

    @Autowired
    RegistrationService registrationService;

    @Test
    public void loginTest() throws Exception {
        String log = "true_login";

        mvc.perform(MockMvcRequestBuilders.get("/checkUniqueLogin?login=" + log)
                .contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().is(200));

        User user = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(user);


        mvc.perform(MockMvcRequestBuilders.get("/checkUniqueLogin?login=" + user.getUsername())
                .contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().is(400));
    }

    @Test
    public void emailTest() throws Exception {
        String email = "unique_email@gmail.com";

        mvc.perform(MockMvcRequestBuilders.get("/checkUniqueEmail?email=" + email)
                .contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().is(200));

        User user = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(user);

        mvc.perform(MockMvcRequestBuilders.get("/checkUniqueEmail?email=" + user.getEmail())
                .contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().is(400));
    }
}

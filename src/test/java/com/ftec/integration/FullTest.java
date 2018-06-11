package com.ftec.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.ChangeSettingController;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.enums.TutorialSteps;
import com.ftec.services.interfaces.TokenService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class FullTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserDAO userDAO;

    @Autowired
    TokenService tokenService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void fullTest() throws Exception {
        //registration
        User user = EntityGenerator.getNewUser();

        MvcResult result = mvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().is(200)).andReturn();
        String token = new JSONObject(result.getResponse().getContentAsString()).getJSONObject("response").getString("token");

        //secured access with valid token
        TutorialSteps step = TutorialSteps.valueOf(mvc.perform(get("/cabinet/tutorial/getCurrentStep")
                .header(TokenService.TOKEN_NAME,token)
        ).andExpect(status().is(200)).andReturn().getResponse().getContentAsString().replaceAll("^\"|\"$", ""));

        assertEquals(TutorialSteps.FIRST, step);

        //logout
        logout(token);
        //is logouted?
        mvc.perform(get("/cabinet/tutorial/getCurrentStep")
                .header(TokenService.TOKEN_NAME,token)
        ).andExpect(status().is(403));

        //log in
        JSONObject userAuth = new JSONObject();
        userAuth.put("password", user.getPassword());
        userAuth.put("username", user.getUsername());

        MvcResult result2 = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userAuth.toString())
        ).andExpect(status().is(200)).andReturn();
        String token2 = new JSONObject(result2.getResponse().getContentAsString()).getJSONObject("response").getString("token");

        mvc.perform(get("/cabinet/tutorial/getCurrentStep")
                .header(TokenService.TOKEN_NAME,token2)
        ).andExpect(status().is(200));

        //change user settings
        ChangeSettingController.UserUpdate updateSetting = new ChangeSettingController.UserUpdate();
        updateSetting.setPassword("new_STRONG_pass123");

        mvc.perform(post("/changeUserSetting")
                .header(TokenService.TOKEN_NAME, token2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(updateSetting))
        ).andExpect(status().is(200));
        user.setPassword(updateSetting.getPassword());

        //logout and test auth with new pass
        logout(token2);

        JSONObject userAuth2 = new JSONObject();
        userAuth2.put("password", user.getPassword());
        userAuth2.put("username", user.getUsername());

        MvcResult result3 = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userAuth2.toString())
        ).andExpect(status().is(200)).andDo(print()).andReturn();

        String token3 = new JSONObject(result3.getResponse().getContentAsString()).getJSONObject("response").getString("token");

    }

    private void logout(String token) throws Exception {
        mvc.perform(post("/logout").header(TokenService.TOKEN_NAME,token))
                .andExpect(status().is(200));
    }
}

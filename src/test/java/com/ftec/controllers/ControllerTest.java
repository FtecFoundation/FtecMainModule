package com.ftec.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.services.Implementations.UserServiceImpl;
import com.ftec.services.TokenService;
import org.junit.Before;
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
import org.springframework.web.util.NestedServletException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    UserDAO dao;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserServiceImpl userService;

    @Before
    public void setUp() {
        dao.deleteAll();
    }

    public static User newUser(String login) {
        User u = new User();
        u.setUsername(login);
        u.setPassword("pass_user1");
        u.setEmail("emaill");

        return u;
    }

    @Test
    public void createValidUser() throws Exception {
        RegistrationController.UserRegistration userRegistration = new RegistrationController.UserRegistration();
        userRegistration.setUsername("tester1");
        userRegistration.setPassword("pass_user1");
        userRegistration.setEmail("emaill@mail.com");
        userRegistration.setSubscribeForNews(true);

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration/registr_test").
                content(asJsonString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated());

        assertTrue(dao.findByUsername("tester1").isPresent());
    }

    @Test(expected = NestedServletException.class)
    public void trySaveDuplicateUsername() throws Exception {
        String userName = "tester2";
        RegistrationController.UserRegistration userRegistration = new RegistrationController.UserRegistration();
        userRegistration.setUsername(userName);
        userRegistration.setPassword("pass_user1");
        userRegistration.setEmail("emaill@mail.com");
        userRegistration.setSubscribeForNews(true);

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration/registr_test").
                content(asJsonString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated());

        //should be status BadRequest
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration/registr_test").
                content(asJsonString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());

        assertTrue(userService.isDuplicateUserName(userName));
    }


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkReturnedToken() throws Exception {
        RegistrationController.UserRegistration userRegistration = new RegistrationController.UserRegistration();
        userRegistration.setUsername("tester3_v2");
        userRegistration.setPassword("pass_user1");
        userRegistration.setEmail("emaill@mail.com");
        userRegistration.setSubscribeForNews(true);

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration/registr_test").
                content(asJsonString(userRegistration)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().exists(TokenService.TOKEN_NAME));
    }

    @Test
    public void registerTwoValidUsers() throws Exception {

        RegistrationController.UserRegistration userRegistration1 = new RegistrationController.UserRegistration();
        userRegistration1.setUsername("tester4_1");
        userRegistration1.setPassword("pass_user1");
        userRegistration1.setEmail("email1@mail.com");
        userRegistration1.setSubscribeForNews(true);

        RegistrationController.UserRegistration userRegistration2 = new RegistrationController.UserRegistration();
        userRegistration2.setUsername("tester4_2");
        userRegistration2.setPassword("pass_user2");
        userRegistration2.setEmail("email2@mail.com");
        userRegistration2.setSubscribeForNews(true);

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration/registr_test").
                content(asJsonString(userRegistration1)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().exists(TokenService.TOKEN_NAME));

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration/registr_test").
                content(asJsonString(userRegistration2)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().exists(TokenService.TOKEN_NAME));

    }


    public static final String regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(regexp);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Test
    public void regExpEmailTest() {
        String validEmails[] = new String[]{
                "alex@yandex.ru",
                "alex-27@yandex.com",
                "alex.27@yandex.com",
                "alex111@test.com",
                "alex.100@test.com.ua",
                "alex@1.com",
                "alex@gmail.com.com",
                "alex+27@gmail.com",
                "alex-27@yandex-test.com"
        };

        for (String email : validEmails) {
            assertTrue(validate(email));
        }

        String unvalidEmails[] = new String[]{
                "devcolibri",
                "alex@.com.ua",
                "alex123@gmail.a",
                "alex123@.com",
                "alex123@.com.com",
                ".alex@devcolibri.com",
                "alex()*@gmail.com",
                "alex@%*.com",
                "alex..2013@gmail.com",
                "alex.@gmail.com",
                "alex@devcolibri@gmail.com",
                "alex@gmail.com.1ua"
        };
        for (String email : unvalidEmails) {
            assertFalse(validate(email));
        }
    }
}
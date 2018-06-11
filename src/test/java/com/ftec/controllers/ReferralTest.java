package com.ftec.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.repositories.ReferralDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.services.interfaces.ReferralService;
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

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ReferralTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ReferralService referralService;

    @Autowired
    ReferralDAO referralDAO;

    @Autowired
    UserDAO userDAO;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void regUserWithRef() throws Exception {
        userDAO.deleteAll();
        referralDAO.deleteAll();

        //Saving User without referrer Id (Main users)
        RegistrationController.UserRegistration userRegWithoutRef = EntityGenerator.getNewRegisrtUser();

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegWithoutRef)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(200));

        User userWithoutRef = userDAO.findByUsername(userRegWithoutRef.getUsername()).get();


        //Saving User with referrer Id (Level one testing)
        RegistrationController.UserRegistration userRegWithRef = EntityGenerator.getNewRegisrtUser();
        userRegWithRef.setReferrerId(userWithoutRef.getId());

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegWithRef)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(200));

        User userWithRef = userDAO.findByUsername(userRegWithRef.getUsername()).get();


        assertNotNull(referralDAO.findReferralLevelOneForUser(userWithRef.getId()));
    }

    @Test
    public void regUserWithRefWhoHaveRef() throws Exception {
        userDAO.deleteAll();
        referralDAO.deleteAll();

        //Saving User without referrer Id (Main users)
        RegistrationController.UserRegistration userRegWithoutRef = EntityGenerator.getNewRegisrtUser();

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegWithoutRef)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(200));

        User userWithoutRef = userDAO.findByUsername(userRegWithoutRef.getUsername()).get();


        //Saving User with referrer Id (Level one testing)
        RegistrationController.UserRegistration userRegWithRef = EntityGenerator.getNewRegisrtUser();
        userRegWithRef.setReferrerId(userWithoutRef.getId());

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegWithRef)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(200));

        User userWithRef1 = userDAO.findByUsername(userRegWithRef.getUsername()).get();


        //Saving User with referrer Id, who have referrer(Level two testing)
        RegistrationController.UserRegistration userRegWithRef2 = EntityGenerator.getNewRegisrtUser();
        userRegWithRef2.setReferrerId(userWithRef1.getId());

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegWithRef2)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(200));

        User userWithRef2 = userDAO.findByUsername(userRegWithRef2.getUsername()).get();


        assertNotNull(referralDAO.findReferralLevelTwoForUser(userWithRef2.getId()));
    }

    @Test
    public void regUserWithRefWhoHaveRefWhoHaveRef() throws Exception {
        userDAO.deleteAll();
        referralDAO.deleteAll();

        //Saving User without referrer Id (Main users)
        RegistrationController.UserRegistration userRegWithoutRef = EntityGenerator.getNewRegisrtUser();

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegWithoutRef)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(200));

        User userWithoutRef = userDAO.findByUsername(userRegWithoutRef.getUsername()).get();


        //Saving User with referrer Id (Level one testing)
        RegistrationController.UserRegistration userRegWithRef = EntityGenerator.getNewRegisrtUser();
        userRegWithRef.setReferrerId(userWithoutRef.getId());

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegWithRef)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(200));

        User userWithRef1 = userDAO.findByUsername(userRegWithRef.getUsername()).get();


        //Saving User with referrer Id, who have referrer(Level two testing)
        RegistrationController.UserRegistration userRegWithRef2 = EntityGenerator.getNewRegisrtUser();
        userRegWithRef2.setReferrerId(userWithRef1.getId());

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegWithRef2)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(200));

        User userWithRef2 = userDAO.findByUsername(userRegWithRef2.getUsername()).get();


        //Saving User with referrer Id, who have referrer, who have referrer (Level three testing)
        RegistrationController.UserRegistration userRegWithRef3 = EntityGenerator.getNewRegisrtUser();
        userRegWithRef3.setReferrerId(userWithRef2.getId());

        mvc.perform(MockMvcRequestBuilders.post("/registration").
                content(objectMapper.writeValueAsString(userRegWithRef3)).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {
                }).andExpect(status().is(200));

        User userWithRef3 = userDAO.findByUsername(userRegWithRef3.getUsername()).get();


        assertNotNull(referralDAO.findReferralLevelThreeForUser(userWithRef3.getId()));
    }

}

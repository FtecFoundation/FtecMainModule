package com.ftec.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ticket;
import com.ftec.entities.User;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.TicketService;
import com.ftec.services.interfaces.TokenService;
import com.ftec.utils.EntityGenerator;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class TicketIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TicketService ticketService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    TokenService tokenService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void test() throws Exception {
        User user = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(user);
        long id = user.getId();
        String token = tokenService.createSaveAndGetNewToken(id);

        assertFalse(ticketService.findByUserId(id).isPresent());

        Ticket ticket = EntityGenerator.getNewTicket();

        MvcResult mvcResult1 = mvc.perform(post("/createTicket")
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(ticket)))
                .andExpect(status().is(200)).andDo(print()).andReturn();
        Integer ticket_id = (Integer)((JSONObject)new JSONObject(mvcResult1.getResponse().getContentAsString()).get("response")).get("ticket_id");

        System.out.println(ticket_id);
        assertTrue(ticketService.findByUserId(id).isPresent());

          mvc.perform(post("/createTicket")
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(EntityGenerator.getNewTicket())))
                .andExpect(status().is(200)).andDo(print()).andReturn();

          mvc.perform(post("/createTicket")
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(EntityGenerator.getNewTicket())))
                .andExpect(status().is(200)).andDo(print()).andReturn();


        registrationService.registerNewUserAccount(EntityGenerator.getNewUser());
        registrationService.registerNewUserAccount(EntityGenerator.getNewUser());
       // assertEquals();
///setSupportedIdForToken


    }
}

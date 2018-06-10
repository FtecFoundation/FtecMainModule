package com.ftec.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ticket;
import com.ftec.entities.User;
import com.ftec.resources.enums.UserRole;
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

import static org.junit.Assert.assertEquals;
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

        assertEquals(0,ticketService.findAllByUserId(id).size());

        Ticket ticket = EntityGenerator.getNewTicket();

        MvcResult mvcResult1 = mvc.perform(post("/createTicket")
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(ticket)))
                .andExpect(status().is(200)).andDo(print()).andReturn();

        long ticket_id = ((Integer)((JSONObject)new JSONObject(mvcResult1.getResponse().getContentAsString()).get("response")).get("ticket_id")).longValue();

        addTwoTickets(id, token);

        testInvalidTicket(token);

        addSupporterToTicket(token, ticket_id, mvcResult1);


        String comment = "some_comment!";
        mvc.perform(post("/support/addComment/" + ticket_id)
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(comment)))
                .andExpect(status().is(200)).andDo(print()).andReturn();
    }

    private void addSupporterToTicket(String token, Long ticket_id, MvcResult mvcResult1) throws Exception {

        User support = EntityGenerator.getNewUser();
        support.setUserRole(UserRole.Support);
        registrationService.registerNewUserAccount(support);


        assertEquals(0, ticketService.findSupportedIdById(ticket_id).intValue());

        mvc.perform(post("/setSupporterIdForTicket")
                .header(TokenService.TOKEN_NAME, token)
                .param("ticket_id", ticket_id.toString())
                .param("supported_id", String.valueOf(support.getId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200)).andDo(print());

        assertEquals(support.getId(), ticketService.findSupportedIdById(ticket_id.longValue()).intValue());

    }


    private void addTwoTickets(Long id, String token) throws Exception {
        assertEquals(1,ticketService.findAllByUserId(id).size());

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

        assertEquals(3,ticketService.findAllByUserId(id).size());

    }

    private void testInvalidTicket(String token) throws Exception {
        Ticket t = EntityGenerator.getNewTicket();
        t.setMessage(null);

        mvc.perform(post("/createTicket")
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(t)))
                .andExpect(status().is(400));
    }
}

package com.ftec.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.TicketController;
import com.ftec.entities.Ticket;
import com.ftec.entities.User;
import com.ftec.resources.enums.TicketStatus;
import com.ftec.resources.enums.UserRole;
import com.ftec.services.interfaces.CommentService;
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

import static org.junit.Assert.*;
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

    @Autowired
    CommentService commentService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void test() throws Exception {
        User user = EntityGenerator.getNewUser();
        user.setUserRole(UserRole.SUPPORT);

        registrationService.registerNewUserAccount(user);
        long id = user.getId();
        String token = tokenService.createSaveAndGetNewToken(id);

        assertEquals(0,ticketService.findAllByUserId(id).size());

        Ticket ticket = EntityGenerator.getNewTicket();

        MvcResult mvcResult1 = mvc.perform(post(TicketController.CREATE_TICKET_URL)
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(ticket)))
                .andExpect(status().is(200)).andDo(print()).andReturn();

        long ticket_id = ((Integer)((JSONObject)new JSONObject(mvcResult1.getResponse().getContentAsString()).get("response")).get("ticket_id")).longValue();

        addTwoTickets(id, token);

        testInvalidTicket(token);

        securityTest(ticket_id, token);

        addSupporterToTicket(token, ticket_id);

        addComment(token, ticket_id);

        changeTicketStatus(token, ticket_id);

        deleteTicket(token, ticket_id);

    }

    private void securityTest(Long ticket_id, String supportToken) throws Exception {
        User not_support = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(not_support);

        String not_support_token = tokenService.createSaveAndGetNewToken(not_support.getId());

        mvc.perform(post(TicketController.ADM_PREF + "/setSupporterIdForTicket")
                .header(TokenService.TOKEN_NAME, not_support_token)
                .param("ticket_id", ticket_id.toString())
                .param("supported_id", String.valueOf(not_support.getId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(403)).andDo(print());

        mvc.perform(post(TicketController.ADM_PREF + "/setSupporterIdForTicket")
                .header(TokenService.TOKEN_NAME, supportToken)
                .param("ticket_id", ticket_id.toString())
                .param("supported_id", String.valueOf(not_support.getId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200)).andDo(print());

        deleteRoleTest();
    }

    private void deleteRoleTest() throws Exception {
        Ticket t = EntityGenerator.getNewTicket();
        t.setUserId(1); //user with id 1 should exist
        ticketService.save(t);

        User not_sup2 = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(not_sup2);
        assertNotEquals(1, not_sup2.getId());

        String not_sup_token2 = tokenService.createSaveAndGetNewToken(not_sup2.getId());

        mvc.perform(post( "/deleteTicket/" + t.getId())
                .header(TokenService.TOKEN_NAME, not_sup_token2)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(403)).andDo(print()).andReturn();
    }

    private void deleteTicket(String token, long ticket_id) throws Exception {
        assertTrue(ticketService.findById(ticket_id).isPresent());
        mvc.perform(post( "/deleteTicket/" + ticket_id)
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200)).andDo(print()).andReturn();
        assertFalse(ticketService.findById(ticket_id).isPresent());
    }

    private void changeTicketStatus(String token, long ticket_id) throws Exception {
        assertEquals(0, ticketService.findById(ticket_id).get().getStatus().ordinal());
        TicketStatus status = TicketStatus.DONE;

        mvc.perform(post( "/changeTicketStatus/" + ticket_id)
                .header(TokenService.TOKEN_NAME, token)
                .content( mapper.writeValueAsString(status))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));

        assertEquals(TicketStatus.DONE.ordinal(), ticketService.findById(ticket_id).get().getStatus().ordinal());

    }

    private void addComment(String token, long ticket_id) throws Exception {
        String comment = "some_comment!";
        mvc.perform(post("/support/addComment/" + ticket_id)
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(comment))
                .andExpect(status().is(200)).andDo(print());
        assertTrue( commentService.getAllByTicketId(ticket_id).stream().
                anyMatch( p ->  p.getMessage().equals(comment))); //can not work if found 2+ comments for this ticket
    }

    private void addSupporterToTicket(String token, Long ticket_id) throws Exception {

        User support = EntityGenerator.getNewUser();
        support.setUserRole(UserRole.SUPPORT);
        registrationService.registerNewUserAccount(support);


        long support_id_before = ticketService.findById(ticket_id).get().getSupporter_id();
        assertNotEquals(support_id_before, support.getId());

        mvc.perform(post( TicketController.ADM_PREF + "/setSupporterIdForTicket")
                .header(TokenService.TOKEN_NAME, token)
                .param("ticket_id", ticket_id.toString())
                .param("supported_id", String.valueOf(support.getId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200)).andDo(print());

        assertEquals(support.getId(), ticketService.findSupportedIdById(ticket_id).longValue());

    }


    private void addTwoTickets(Long id, String token) throws Exception {
        assertEquals(1,ticketService.findAllByUserId(id).size());

        mvc.perform(post(TicketController.CREATE_TICKET_URL)
              .header(TokenService.TOKEN_NAME, token)
              .contentType(MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsString(EntityGenerator.getNewTicket())))
              .andExpect(status().is(200)).andDo(print()).andReturn();

        mvc.perform(post(TicketController.CREATE_TICKET_URL)
              .header(TokenService.TOKEN_NAME, token)
              .contentType(MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsString(EntityGenerator.getNewTicket())))
              .andExpect(status().is(200)).andDo(print()).andReturn();

        assertEquals(3,ticketService.findAllByUserId(id).size());

    }

    private void testInvalidTicket(String token) throws Exception {
        Ticket t = EntityGenerator.getNewTicket();
        t.setMessage(null);

        mvc.perform(post(TicketController.CREATE_TICKET_URL)
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(t)))
                .andExpect(status().is(400));
    }
}

package com.ftec.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ticket;
import com.ftec.entities.User;
import com.ftec.resources.enums.TicketCategory;
import com.ftec.resources.enums.TicketStatus;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.TicketService;
import com.ftec.utils.EntityGenerator;
import org.json.JSONArray;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class TicketTest {
    @Autowired
    TicketService ticketService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    TokenService tokenService;

    @Test
    public void ticketSetSupporterId(){
        Ticket t = new Ticket();
        t.setMessage("Message");
        t.setStatus(TicketStatus.NEW);
        t.setSubject("subject");

        ticketService.save(t);

        User user = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(user);

        assertEquals(ticketService.findById(t.getId()).get().getSupporter_id(), 0);

        ticketService.setTicketSupport(t.getId(), 2);

        assertEquals(ticketService.findById(t.getId()).get().getSupporter_id(), 2);


    }

    @Test
    public void getAllTickets() throws Exception {
        ticketService.deleteAll();

        Ticket t = EntityGenerator.getNewTicket();
        ticketService.save(t);

        Ticket t2 = EntityGenerator.getNewTicket();
        ticketService.save(t2);

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/support/getAllTickets")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        String content = mvcResult1.getResponse().getContentAsString();

        JSONObject response = new JSONObject(content);
        JSONObject resp = (JSONObject) response.get("response");
        JSONArray j = resp.getJSONArray("java.util.ArrayList");
        JSONObject o = (JSONObject)j.get(1);

        String subject = (String) o.get("subject");

        String val = o.toString();
        Ticket ticket = mapper.readValue(val, Ticket.class);

        assertEquals(t2.getSubject(), ticket.getSubject());
    }

    @Test
    public void createControllerTest() throws Exception {
        Ticket t = EntityGenerator.getNewTicket();

        User user = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(user);
        String token = tokenService.createSaveAndGetNewToken(user.getId());

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/createTicket")
                .content(mapper.writeValueAsString(t))
                .header(TokenService.TOKEN_NAME,token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        //ddd
        System.out.println("test");
        assertEquals(ticketService.findByUserId(user.getId()).get().getUserId(), user.getId());
    }
}

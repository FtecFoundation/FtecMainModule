package com.ftec.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ticket;
import com.ftec.entities.User;
import com.ftec.resources.Resources;
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
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
        Ticket t = EntityGenerator.getNewTicket();

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


        List<String> subjects = new ArrayList<>();
        subjects.add(t.getSubject());
        subjects.add(t2.getSubject());

        boolean tr = true;

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/support/getAllTickets")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(Resources.doPrintStatic ? Resources.doPrintStatic ? print() : (ResultHandler) result -> {} : (ResultHandler) result -> {}).andExpect(status().isOk()).andReturn();

        String content = mvcResult1.getResponse().getContentAsString();

        JSONObject response = new JSONObject(content);
        JSONObject resp = (JSONObject) response.get("response");
        JSONArray j = resp.getJSONArray("Tickets");

        int corresponding = 2;
        int correspond = 0;

        for(int i = 0; i < j.length(); i++){
            String val = j.get(i).toString();
            Ticket ticket = mapper.readValue(val, Ticket.class);
            if(subjects.contains(ticket.getSubject()))correspond++;
        }


        assertEquals(correspond,corresponding);

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
                .andDo(Resources.doPrintStatic ? print() : (ResultHandler) result -> {}).andExpect(status().isOk());

        //ddd
        System.out.println("test");
        assertEquals(ticketService.findByUserId(user.getId()).get().getUserId(), user.getId());
    }
}

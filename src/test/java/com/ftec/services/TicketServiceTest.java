package com.ftec.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ticket;
import com.ftec.entities.User;
import com.ftec.exceptions.TicketException;
import com.ftec.resources.Resources;
import com.ftec.resources.enums.TicketStatus;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.TicketService;
import com.ftec.services.interfaces.TokenService;
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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class TicketServiceTest {

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

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get("/support/getAllTickets")
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
    public void deleteAll(){
        ticketService.deleteAll();
        assertEquals(0, ticketService.getAll().size());
        ticketService.save(EntityGenerator.getNewTicket());
        ticketService.save(EntityGenerator.getNewTicket());
        ticketService.save(EntityGenerator.getNewTicket());
        assertEquals(3, ticketService.getAll().size());
        ticketService.deleteAll();
        assertEquals(0, ticketService.getAll().size());

    }

    @Test
    public void deleteById() throws TicketException {
        Ticket t = EntityGenerator.getNewTicket();
        long userId = 155;
        t.setUserId(userId);
        ticketService.save(t);
        assertTrue(ticketService.findById(t.getId()).isPresent());

        ticketService.deleteById(t.getId(),userId);
        assertFalse(ticketService.findById(t.getId()).isPresent());

    }

    @Test(expected = TicketException.class)
    public void deleteByIdWithoutAuthority() throws TicketException {
        Ticket t = EntityGenerator.getNewTicket();
        long userId = 155;
        t.setUserId(userId);
        ticketService.save(t);
        assertTrue(ticketService.findById(t.getId()).isPresent());

        User simple_user = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(simple_user);

        ticketService.deleteById(t.getId(),simple_user.getId()); // we expect the exception

    }

    @Test
    public void changeTicketStatus() throws TicketException {
        Ticket t = EntityGenerator.getNewTicket();
        ticketService.save(t);

        @NotNull TicketStatus old_status = t.getStatus();

        ticketService.changeTicketStatus(t.getId(), TicketStatus.CLOSED);

        assertNotEquals(old_status, ticketService.findById(t.getId()).get().getStatus());
    }

    @Test
    public void findAllByUserId(){
        User u = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(u);

        Ticket t1 = EntityGenerator.getNewTicket();
        t1.setUserId(u.getId());

        Ticket t2 = EntityGenerator.getNewTicket();
        t2.setUserId(u.getId());

        assertEquals(0,ticketService.findAllByUserId(u.getId()).size());
        ticketService.save(t1);
        ticketService.save(t2);
        assertEquals(2,ticketService.findAllByUserId(u.getId()).size());
    }

    @Test
    public void addTicket() throws TicketException {
        Ticket t = EntityGenerator.getNewTicket();
        String token = tokenService.createSaveAndGetNewToken(25l);
        ticketService.addTicket(t, token);

        assertTrue(ticketService.findById(t.getId()).isPresent());
    }

    @Test
    public void setTicketSupportedId(){
        Ticket t = EntityGenerator.getNewTicket();
        ticketService.save(t);

        assertEquals(0, t.getSupporter_id());

        ticketService.setTicketSupport(t.getId(), 2);
    }

}
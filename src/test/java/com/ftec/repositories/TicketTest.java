package com.ftec.repositories;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ticket;
import com.ftec.entities.User;
import com.ftec.resources.enums.TicketStatus;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
public class TicketTest {
    @Autowired
    TicketDAO ticketDAO;

    @Autowired
    RegistrationService registrationService;

//    @Autowired
//    Mockmvc mvc;

    @Test
    public void ticketSetSupporterId(){
        Ticket t = new Ticket();
        t.setMessage("Message");
        t.setStatus(TicketStatus.NEW);
        t.setSubject("subject");

        ticketDAO.save(t);

        User user = EntityGenerator.getNewUser();
        registrationService.registerNewUserAccount(user);

        assertEquals(ticketDAO.findById(t.getId()).get().getSupporter_id(), 0);

        ticketDAO.setTicketSupport(t.getId(), 2);

        assertEquals(ticketDAO.findById(t.getId()).get().getSupporter_id(), 2);


    }
}

package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Comment;
import com.ftec.entities.Ticket;
import com.ftec.repositories.CommentDAO;
import com.ftec.repositories.TicketDAO;
import com.ftec.services.interfaces.CommentService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class CommentServiceTest {

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    CommentService commentService;

    @Autowired
    TicketDAO ticketDAO;

    @Test
    public void addCommentToTicketTest() throws Exception {
        Ticket ticket = EntityGenerator.getNewTicket();
        ticketDAO.save(ticket);
        String jsonMessage = "{\n\t\"comment\":\"comment from service TEST\"\n}";
        commentService.addCommentToTicket(ticket.getId(), new Date(), jsonMessage, 2);

        assertNotNull(commentDAO.findByTicketId(ticket.getId()));

        ticketDAO.deleteAll();
        commentDAO.deleteAll();
    }
}

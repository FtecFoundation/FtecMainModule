package com.ftec.repositories;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Comment;
import com.ftec.entities.Ticket;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
public class CommentDAOTest {

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    TicketDAO ticketDAO;


    @Test
    public void saveCommentToTicketTest() throws Exception {

        Ticket ticket = EntityGenerator.getNewTicket();
        ticketDAO.save(ticket);
        commentDAO.saveCommentToTicket(ticket.getId(), new Date(), "Test comment from TEST", 2);

        assertNotNull(commentDAO.findByTicketId(ticket.getId()));
        ticketDAO.deleteAll();
        commentDAO.deleteAll();
    }

    @Test
    public void findCommentByTicketIdTest() throws Exception {
        Ticket ticket = EntityGenerator.getNewTicket();
        ticketDAO.save(ticket);
        commentDAO.saveCommentToTicket(ticket.getId(), new Date(), "Test comment from TEST", 2);

        Comment finded = commentDAO.findByTicketId(ticket.getId());
        assertNotNull(finded);
        ticketDAO.deleteAll();
        commentDAO.deleteAll();
    }
}

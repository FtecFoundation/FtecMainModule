package com.ftec.services.Implementations;

import com.ftec.repositories.CommentDAO;
import com.ftec.services.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDAO commentDAO;

    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO) {
        this.commentDAO = commentDAO;
    }


    @Override
    @Transactional
    public void addCommentToTicket(long ticketId, Date creationDate, String jsonMessage, long userId) {
        commentDAO.saveCommentToTicket(ticketId, creationDate, jsonMessage, userId);
    }

}

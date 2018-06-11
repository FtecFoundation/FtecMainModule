package com.ftec.services.Implementations;

import com.ftec.entities.Comment;
import com.ftec.repositories.CommentDAO;
import com.ftec.services.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDAO commentDAO;

    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO) {
        this.commentDAO = commentDAO;
    }


    @Override
    @Transactional
    public void addCommentToTicket(long ticketId, Date creationDate, String message, long userId) {
        commentDAO.saveCommentToTicket(ticketId, creationDate, message, userId);
    }

    @Override
    @Transactional
    public void delete(long commentId) {
        commentDAO.deleteCommentById(commentId);
    }

    @Override
    public Optional<Comment> getById(long commentId) {
        return commentDAO.findById(commentId);
    }

    @Override
    @Transactional
    public void update(Comment comment) {
        commentDAO.save(comment);
    }

}

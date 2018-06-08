package com.ftec.services.Implementations;

import com.ftec.entities.Comment;
import com.ftec.repositories.CommentDAO;
import com.ftec.services.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDAO commentDAO;

    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO) {
        this.commentDAO = commentDAO;
    }

    @Override
    public void save(Comment comment) {
        commentDAO.save(comment);
    }

    @Override
    public void delete(Comment comment) {
        commentDAO.delete(comment);
    }
}

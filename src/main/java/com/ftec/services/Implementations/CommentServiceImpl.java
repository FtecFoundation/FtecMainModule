package com.ftec.services.Implementations;

import com.ftec.entities.Comment;
import com.ftec.repositories.CommentDAO;
import com.ftec.services.interfaces.CommentService;
import org.json.JSONObject;
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
        String message = formatFromJsonToString(jsonMessage);
        commentDAO.saveCommentToTicket(ticketId, creationDate, message, userId);
    }

    @Override
    public void delete(Comment comment) {
        commentDAO.delete(comment);
    }

    private String formatFromJsonToString(String jsonMessage) {
        JSONObject object = new JSONObject(jsonMessage);
        return object.getString("comment");
    }
}

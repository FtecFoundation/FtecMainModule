package com.ftec.services.interfaces;

import com.ftec.entities.Comment;

import java.util.Date;

public interface CommentService {

    void addCommentToTicket(long ticketId, Date creationDate, String jsonMessage, long userId);

    void delete(Comment comment);

}

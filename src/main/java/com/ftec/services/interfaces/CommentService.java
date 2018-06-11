package com.ftec.services.interfaces;

import com.ftec.entities.Comment;

import java.util.Date;
import java.util.Optional;

public interface CommentService {

    void addCommentToTicket(long ticketId, Date creationDate, String message, long userId);

    void delete(long commentId);

    Optional<Comment> getById(long commentId);

    void update(Comment comment);
}

package com.ftec.services.interfaces;

import java.util.Date;

public interface CommentService {

    void addCommentToTicket(long ticketId, Date creationDate, String jsonMessage, long userId);

}

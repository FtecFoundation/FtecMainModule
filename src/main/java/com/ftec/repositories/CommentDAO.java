package com.ftec.repositories;

import com.ftec.entities.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface CommentDAO extends CrudRepository<Comment, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO comment (ticket_id, creation_date, message, user_id) VALUES (?1, ?2, ?3, ?4);", nativeQuery = true)
    void saveCommentToTicket(long ticketId, Date creationDate, String message, long userId);

    @Query(value = "SELECT * FROM comment WHERE ticket_id = ?1", nativeQuery = true)
    Comment findByTicketId(long id);
}

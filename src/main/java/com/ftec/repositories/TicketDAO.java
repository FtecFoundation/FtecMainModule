package com.ftec.repositories;

import com.ftec.entities.Ticket;
import com.ftec.resources.enums.TicketStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDAO extends CrudRepository<Ticket, Long> {

    List<Ticket> findAll();

    @Modifying
    @Query(value = "update ticket set supporter_id = ?2 where id = ?1", nativeQuery = true)
    void setTicketSupport(long tiket_id, long support_id);

    List<Ticket> findAllByUserId(long id);

    @Query(value = "select supporter_id from ticket where id = ?1", nativeQuery = true)
    Long findSupportedIdById(Long ticket_id);

    @Query(value = "update ticket set status = ?2 where id = ?1", nativeQuery = true)
    void changeTicketStatus(long ticket_id, TicketStatus status);
}

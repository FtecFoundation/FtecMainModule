package com.ftec.repositories;

import com.ftec.entities.Ticket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketDAO extends CrudRepository<Ticket, Long> {

    List<Ticket> findAll();

    @Modifying
    @Query(value = "update ticket set supporter_id = ?2 where id = ?1", nativeQuery = true)
    void setTicketSupport(long tiket_id, long support_id);

    Optional<Ticket> findByUserId(long id);
}

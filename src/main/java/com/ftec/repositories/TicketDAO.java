package com.ftec.repositories;

import com.ftec.entities.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDAO extends CrudRepository<Ticket, Long> {

    List<Ticket> findAll();
}

package com.ftec.services.interfaces;

import com.ftec.entities.Ticket;
import com.ftec.exceptions.TicketException;
import com.ftec.resources.enums.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    List<Ticket> getAll();

    void save(Ticket ticket);

    void setTicketSupport(long tiket_id, long support_id);

    void deleteAll();

    Optional<Ticket> findById(long id);

    long addTicket(Ticket ticket, String token) throws TicketException;

    List<Ticket> findAllByUserId(long id);

    Long findSupportedIdById(Long ticket_id);

    void changeTicketStatus(long ticket_id, TicketStatus status) throws TicketException;

    void deleteById(long ticket_id) throws TicketException;
}

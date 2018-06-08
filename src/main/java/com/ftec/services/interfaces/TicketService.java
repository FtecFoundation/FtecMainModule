package com.ftec.services.interfaces;

import com.ftec.entities.Ticket;

import java.util.List;

public interface TicketService {

    List<Ticket> getAll();

    void save(Ticket ticket);
}

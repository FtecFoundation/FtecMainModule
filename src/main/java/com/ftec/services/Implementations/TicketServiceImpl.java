package com.ftec.services.Implementations;

import com.ftec.entities.Ticket;
import com.ftec.repositories.TicketDAO;
import com.ftec.services.interfaces.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private TicketDAO ticketDAO;

    public TicketServiceImpl(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    @Override
    public List<Ticket> getAll() {
        return ticketDAO.findAll();
    }

    @Override
    @Transactional
    public void save(Ticket ticket) {
        ticketDAO.save(ticket);
    }
}

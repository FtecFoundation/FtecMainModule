package com.ftec.services.Implementations;

import com.ftec.entities.Ticket;
import com.ftec.exceptions.TicketException;
import com.ftec.repositories.TicketDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private TicketDAO ticketDAO;
    private UserDAO userDAO;

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

    @Transactional
    @Override
    public void setTicketSupport(long tiket_id, long support_id) {
        ticketDAO.setTicketSupport(tiket_id, support_id);
    }

    @Override
    public void deleteAll() {
        ticketDAO.deleteAll();
    }

    @Override
    public Optional<Ticket> findById(long id) {
        return ticketDAO.findById(id);
    }

    /*
     * @return ticket`s id
     */
    @Override
    public long addTicket(Ticket ticket, String token) throws TicketException {
        ticket.setUserId(TokenService.getUserIdFromToken(token));
        ticketDAO.save(ticket);
        return ticket.getId();
    }

    @Override
    public Optional<Ticket> findByUserId(long id) {
        return ticketDAO.findByUserId(id);
    }
}

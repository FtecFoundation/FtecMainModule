package com.ftec.services.Implementations;

import com.ftec.entities.Ticket;
import com.ftec.exceptions.TicketException;
import com.ftec.repositories.TicketDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.enums.TicketStatus;
import com.ftec.resources.enums.UserRole;
import com.ftec.services.interfaces.TicketService;
import com.ftec.services.interfaces.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketDAO ticketDAO;
    private final UserDAO userDAO;

    public TicketServiceImpl(TicketDAO ticketDAO, UserDAO userDAO) {
        this.ticketDAO = ticketDAO;
        this.userDAO = userDAO;
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
        ticket.setCreationDate(new Date());
        ticketDAO.save(ticket);
        return ticket.getId();
    }

    @Override
    public List<Ticket> findAllByUserId(long id) {
        return ticketDAO.findAllByUserId(id);
    }

    @Override
    public Long findSupportedIdById(Long ticket_id) {
        return ticketDAO.findSupportedIdByTicketId(ticket_id);
    }

    @Override
    @Transactional
    public void changeTicketStatus(long ticket_id, TicketStatus status) throws TicketException{
        checkIfTicketExist(ticket_id);

        ticketDAO.changeTicketStatus(ticket_id, status.ordinal());
    }

    @Override
    public void deleteById(long ticket_id, Long senderId) throws TicketException {
        checkIfTicketExist(ticket_id);

        if(ticketDAO.findById(ticket_id).get().getUserId() != senderId){
            if(!userDAO.findById(senderId).get().getUserRole().equals(UserRole.SUPPORT)) throw new TicketException("Forbidden!");
        }

        ticketDAO.deleteById(ticket_id);
    }

    private void checkIfTicketExist(long ticket_id) throws TicketException {
        if(!ticketDAO.findById(ticket_id).isPresent()) throw new TicketException("Ticket with this id not found!");
    }
}

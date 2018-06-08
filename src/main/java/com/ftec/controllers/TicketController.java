package com.ftec.controllers;

import com.ftec.entities.Ticket;
import com.ftec.exceptions.TicketException;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("support/getAllTickets")
    public MvcResponse getAllTickets() {
        List<Ticket> allTicketsFromDB = ticketService.getAll();
        return new MvcResponse(200, allTicketsFromDB);
    }

    @PostMapping("/createTicket")
    public MvcResponse addTicket(@RequestBody Ticket ticket, HttpServletRequest request){
       try {
           ticketService.addTicket(ticket, request.getHeader(TokenService.TOKEN_NAME));
       } catch (TicketException e){
           return new MvcResponse(200,e.getMessage());
       } catch (Exception e){
           return new MvcResponse(200,"Unexpected error");
       }
        return new MvcResponse(200);
    }
}

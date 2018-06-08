package com.ftec.controllers;

import com.ftec.entities.Ticket;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

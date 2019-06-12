package com.ftec.controllers;

import com.ftec.entities.Ticket;
import com.ftec.entities.User;
import com.ftec.exceptions.TicketException;
import com.ftec.resources.enums.TicketStatus;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.CommentService;
import com.ftec.services.interfaces.TicketService;
import com.ftec.services.interfaces.TokenService;
import com.ftec.services.interfaces.UserService;
import com.ftec.utils.files.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TicketController {

    private final TicketService ticketService;
    private final CommentService commentService;
    private final UserService userService;

    public static final String ADM_PREF = "/manage";
    public static final String CREATE_TICKET_URL = "/createTicket";

    @Autowired
    public TicketController(TicketService ticketService, CommentService commentService, UserService userService) {
        this.ticketService = ticketService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("support/getAllTickets")
    public MvcResponse getAllTickets() {
        List<Ticket> allTicketsFromDB = ticketService.getAll();
        return new MvcResponse(200, "Tickets", allTicketsFromDB);
    }

    @PostMapping("support/addComment/{ticketId}")
    public MvcResponse addComment(@PathVariable("ticketId") long ticketId, @RequestBody String message, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(TokenService.TOKEN_NAME);
        Optional<User> commentator = userService.getById(TokenService.getUserIdFromToken(token));
        if (commentator.isPresent()) {
            Optional<Ticket> ticketById = ticketService.findById(ticketId);
            if (ticketById.isPresent()) {
                if (ticketById.get().getStatus() != TicketStatus.CLOSED) {
                    long commentatorId = commentator.get().getId();
                    commentService.addCommentToTicket(ticketId, new Date(), message, commentatorId);
                    return new MvcResponse(200);
                }
            } else {
                response.setStatus(400);
                return MvcResponse.getMvcErrorResponse(400, "Ticket not found");
            }
        }
        response.setStatus(400);
        return MvcResponse.getMvcErrorResponse(400, "User not found");
    }

    @PostMapping(CREATE_TICKET_URL)
    public MvcResponse addTicket(@RequestBody Ticket ticket, BindingResult br, HttpServletRequest request, HttpServletResponse response) {
        if (br.hasErrors()) {
            response.setStatus(400);
            return new MvcResponse(400, br.getAllErrors());
        }

        try {
            Long ticket_id = ticketService.addTicket(ticket, request.getHeader(TokenService.TOKEN_NAME));
            return new MvcResponse(200, "ticket_id", ticket_id);
        } catch (TicketException e) {
            response.setStatus(400);
            return new MvcResponse(400, e.getMessage());
        } catch (Exception e) {
            Logger.logException("Unexpected exception: ", e, true);
            response.setStatus(400);
            return new MvcResponse(400, "Unexpected error");
        }
    }


    @PostMapping(value = ADM_PREF + "/setSupporterIdForTicket", consumes = "application/json", produces = "application/json")
    public MvcResponse setSupportedIdForToken(@RequestParam("ticket_id") long ticket_id, @RequestParam("supported_id") long supporter_id) {
        ticketService.setTicketSupport(ticket_id, supporter_id);
        return new MvcResponse(200);
    }

    @PostMapping(value = "/changeTicketStatus/{ticket_id}", consumes = "application/json", produces = "application/json")
    public MvcResponse changeTicketStatus(@PathVariable("ticket_id") long ticket_id, @RequestBody TicketStatus status, HttpServletResponse response) {
        try {
            ticketService.changeTicketStatus(ticket_id, status);
        } catch (TicketException e) {
            response.setStatus(400);
            return new MvcResponse(400, e.getMessage());
        } catch (Exception e) {
            Logger.logException("//", e, true);
            response.setStatus(400);
            return new MvcResponse(400, "Unexpected error");
        }
        return new MvcResponse(200);
    }

    @PostMapping(value = "/deleteTicket/{ticket_id}", consumes = "application/json", produces = "application/json")
    public MvcResponse deleteTicket(@PathVariable("ticket_id") long ticket_id, HttpServletRequest request, HttpServletResponse response) {
        try {
            ticketService.deleteById(ticket_id, TokenService.getUserIdFromToken(request.getHeader(TokenService.TOKEN_NAME)));

        } catch (TicketException e) {
            response.setStatus(403);
            return new MvcResponse(403, e.getMessage());

        } catch (Exception e) {
            Logger.logException("//", e, true);
            response.setStatus(400);
            return new MvcResponse(400, "Unexpected error");
        }
        return new MvcResponse(200);
    }
}

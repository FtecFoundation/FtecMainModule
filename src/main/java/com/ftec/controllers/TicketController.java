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
import com.ftec.utils.Logger;
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
    public MvcResponse addComment(@PathVariable("ticketId") long ticketId, @RequestBody String jsonComment, HttpServletRequest request) {
        String token = request.getHeader(TokenService.TOKEN_NAME);
        Optional<User> commentator = userService.getById(TokenService.getUserIdFromToken(token));
        if (commentator.isPresent()) {
            long commentatorId = commentator.get().getId();
            commentService.addCommentToTicket(ticketId, new Date(), jsonComment, commentatorId);
            return new MvcResponse(200);
        }

        return MvcResponse.getMvcErrorResponse(400, "User not found");
    }

    @PostMapping("/createTicket")
    public MvcResponse addTicket(@RequestBody Ticket ticket, BindingResult br, HttpServletRequest request, HttpServletResponse response) {
        if(br.hasErrors()){
            response.setStatus(400);
            return new MvcResponse(400, br.getAllErrors());
        }

        try {
            Long ticket_id = ticketService.addTicket(ticket, request.getHeader(TokenService.TOKEN_NAME));
            return new MvcResponse(200, "ticket_id", ticket_id);
        } catch (TicketException e) {
            return new MvcResponse(200, e.getMessage());
        } catch (Exception e) {
            Logger.logException("Unexpected exception: ", e, true);
            response.setStatus(400);
            return new MvcResponse(400, "Unexpected error");
        }
    }


    @PostMapping("/setSupporterIdForTicket")
    public MvcResponse setSupportedIdForToken(@RequestParam("ticket_id") long ticket_id, @RequestParam("supported_id") long supporter_id){
        ticketService.setTicketSupport(ticket_id, supporter_id);
        return new MvcResponse(200);
    }

    @PostMapping("/changeTicketStatus/{ticket_id}")
    public MvcResponse changeTicketStatus(@RequestParam("new_status") TicketStatus status, @PathVariable("ticket_id") long ticket_id, HttpServletResponse response){
        try {
            ticketService.changeTicketStatus(ticket_id, status);
        } catch (TicketException e){
            response.setStatus(400);
            return new MvcResponse(400, e.getMessage());
        }
        return new MvcResponse(200);
    }
}

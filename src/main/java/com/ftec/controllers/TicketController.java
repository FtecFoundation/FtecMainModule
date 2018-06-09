package com.ftec.controllers;

import com.ftec.entities.Ticket;
import com.ftec.entities.User;
import com.ftec.exceptions.TicketException;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.CommentService;
import com.ftec.services.interfaces.TicketService;
import com.ftec.services.interfaces.TokenService;
import com.ftec.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TicketController {

    private final TicketService ticketService;
    private final CommentService commentService;
    private final UserService userService;

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
    public MvcResponse addTicket(@RequestBody Ticket ticket, HttpServletRequest request) {
        try {
            Long ticket_id = ticketService.addTicket(ticket, request.getHeader(TokenService.TOKEN_NAME));
            return new MvcResponse(200, "ticket_id", ticket_id);
        } catch (TicketException e) {
            return new MvcResponse(200, e.getMessage());
        } catch (Exception e) {
            return new MvcResponse(200, "Unexpected error");
        }
    }

    @Autowired
    public TicketController(TicketService ticketService, CommentService commentService, UserService userService) {
        this.ticketService = ticketService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/setSupportedIdForToken")
    public MvcResponse setSupportedIdForToken(@RequestParam("ticket_id") long ticket_id, @RequestParam("supported_id") long supporter_id){
        //should I do any check?
        ticketService.setTicketSupport(ticket_id, supporter_id);
        return new MvcResponse(200);
    }
}

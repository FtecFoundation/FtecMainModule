package com.ftec.entities;

import com.ftec.resources.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@AllArgsConstructor
@Table
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String subject;

    @NotNull
    @Size(max = 2000)
    private String message;

    @NotNull
    private TicketStatus status = TicketStatus.NEW;
}

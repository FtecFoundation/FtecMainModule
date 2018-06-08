package com.ftec.entities;

import com.ftec.resources.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long userId;

    @NotNull
    @Size(max = 100)
    private String subject;

    @NotNull
    @Size(max = 2000)
    private String message;

    @NotNull
    private TicketStatus status = TicketStatus.NEW;

    private long supporter_id;

}

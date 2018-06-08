package com.ftec.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TicketException extends Exception {
    public TicketException(String message){
        super(message);
    }
}

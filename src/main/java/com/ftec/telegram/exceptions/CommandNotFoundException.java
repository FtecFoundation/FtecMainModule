package com.ftec.telegram.exceptions;

public class CommandNotFoundException extends Exception {
    public CommandNotFoundException() {
        super("Command wasn't found");
    }
}

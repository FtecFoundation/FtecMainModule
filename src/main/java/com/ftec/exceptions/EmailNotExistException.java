package com.ftec.exceptions;

public class EmailNotExistException extends Exception{
    public EmailNotExistException(){
        super("User with this email does not exist!");
    }
}

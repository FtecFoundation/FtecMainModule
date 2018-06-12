package com.ftec.exceptions;

public class InvalidHashException extends Exception {
    public InvalidHashException(){}

    public InvalidHashException(String msg){
        super(msg);
    }
}

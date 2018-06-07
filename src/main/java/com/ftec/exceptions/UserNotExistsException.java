package com.ftec.exceptions;

public class UserNotExistsException extends Exception {
    public UserNotExistsException(){
        super("The user with this username does not exist!");
    }
    public UserNotExistsException(String msg){
        super(msg);
    }
}

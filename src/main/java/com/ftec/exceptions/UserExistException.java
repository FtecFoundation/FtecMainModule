package com.ftec.exceptions;

public class UserExistException extends Exception {

    @Override
    public String getMessage() {
        return "User with this username already exist!";
    }
}

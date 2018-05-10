package com.ftec.telegram.exceptions;

public class BotRegisteredException extends Exception{
    public BotRegisteredException() {
        super("Bot is already registered for user;");
    }
}

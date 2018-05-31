package com.ftec.modules.telegram.exceptions;

public class BotNotRegisteredException extends Exception {
    public BotNotRegisteredException() {
        super("Bot not registered.");
    }
}

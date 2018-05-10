package com.ftec.resources;

public enum BotStatus {
    NONE, DISABLED, PENDING, READY;

    public String getColor(){
        switch (this){
            case READY:
                return "green";
            case PENDING:
                return "yellow";
            case DISABLED:
                return "red";
        }
        return "";
    }
}

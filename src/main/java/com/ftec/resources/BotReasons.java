package com.ftec.resources;

public enum BotReasons {
    NONE(BotStatus.READY), wrongAPI(BotStatus.DISABLED), insufficientCurrency(BotStatus.DISABLED),insufficientCredits(BotStatus.PENDING);
    private BotStatus botStatus;

    BotReasons(BotStatus isAllowed) {
        this.botStatus = isAllowed;
    }

    public BotStatus getBotStatus(){
        return botStatus;
    }
}

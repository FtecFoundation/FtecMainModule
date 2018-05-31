package com.ftec.modules.telegram.resources;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.ftec.modules.telegram.resources.Commands.*;

public enum Stages {
    MAIN(null, "telegram.main.welcomeStage");

    public Stages previousStage;
    public String stageText;

    Stages(Stages previousStage, String stageText) {
        this.previousStage = previousStage;
        this.stageText = stageText;
    }

    public ReplyKeyboardMarkup getKeyboardMarkup(String locale){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(getMarkup(locale));
        return replyKeyboardMarkup;
    }

    private List<KeyboardRow> getMarkup(String locale){
        switch (this){
            case MAIN:
                return getMainMarkup(locale);
                default:return null;
        }
    }

    private List<KeyboardRow> getMainMarkup(String locale){
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        boolean isRu = locale.equals("ru");
        firstRow.add(isRu?balanceKeyboard_ru:balanceKeyboard);
        firstRow.add(isRu?botsModule_ru:botsModule);
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(isRu?arbitrageAssistant_ru:arbitrageAssistant);
        secondRow.add(isRu?socialAssistant_ru:socialAssistant);
        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add(isRu?changeLanguage_ru:changeLanguage);
        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboard.add(thirdRow);
        return keyboard;
    }
}

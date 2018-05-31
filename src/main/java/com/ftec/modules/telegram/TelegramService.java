package com.ftec.modules.telegram;

import com.ftec.entities.TelegramSettings;
import com.ftec.repositories.TelegramSettingsDAO;
import com.ftec.resources.TelegramNotifications;
import com.ftec.modules.telegram.exceptions.WrongCodeException;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Random;

@Service
public class TelegramService {
    private final TelegramSettingsDAO telegramSettings;

    public TelegramService(TelegramSettingsDAO telegramSettings) {
        this.telegramSettings = telegramSettings;
    }

    public void linkChatWithUser(long userId, long chatId, String username, String code) throws WrongCodeException {
        TelegramSettings settings = telegramSettings.getByUserId(userId);
        if(!settings.getAccessCode().equals(code)) throw new WrongCodeException("Code you've provided doesn't match with an account you want to link");
        settings.setLinkedUserChatId(chatId);
        settings.setLinkedUsername(username);
        settings.setEnabled(true);
        telegramSettings.save(settings);
    }

    public String createCode(long userId){
        String code = createTelegramCode(userId);
        TelegramSettings settings = telegramSettings.getByUserId(userId);
        settings.setAccessCode(code);
        telegramSettings.save(settings);
        return code;
    }
    public void disableTelegram(){
        //TODO implement
        throw new NotImplementedException();
    }

    public TelegramSettings getSettings(){
        //TODO implement
        throw new NotImplementedException();
    }
    public TelegramSettings getSettings(long userId){
        return telegramSettings.getByUserId(userId);
    }
    public void updateSettings(List<TelegramNotifications> telegramSettings){
        //TODO implement
        throw new NotImplementedException();    }

    private String createTelegramCode(long userId){
        return userId+"_"+(int)(new Random().nextDouble()*10000);
    }
}

package com.ftec.telegram;

import com.ftec.entities.TelegramSettings;
import com.ftec.repositories.TelegramSettingsDAO;
import com.ftec.resources.TelegramNotifications;
import com.ftec.telegram.exceptions.WrongCodeException;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;

@Service
public class TelegramService {
    private final TelegramSettingsDAO telegramSettings;

    public TelegramService(TelegramSettingsDAO telegramSettings) {
        this.telegramSettings = telegramSettings;
    }

    @Transactional
    public void linkChatWithUser(long userId, long chatId, String username, String code) throws WrongCodeException {
        TelegramSettings settings = telegramSettings.get(userId);
        if(!settings.getAccessCode().equals(code)) throw new WrongCodeException("Code you've provided doesn't match with an account you want to link");
        settings.setLinkedUserChatId(chatId);
        settings.setLinkedUsername(username);
        settings.setEnabled(true);
        telegramSettings.update(settings);
    }

    @Transactional
    public String createCode(long userId){
        String code = createTelegramCode(userId);
        telegramSettings.saveCode(userId, code);
        return code;
    }
    @Transactional
    public void disableTelegram(){
        //TODO implement
        throw new NotImplementedException();
    }

    @Transactional
    public TelegramSettings getSettings(){
        //TODO implement
        throw new NotImplementedException();
    }
    @Transactional
    public TelegramSettings getSettings(long userId){
        return telegramSettings.get(userId);
    }
    @Transactional
    public void updateSettings(List<TelegramNotifications> telegramSettings){
        //TODO implement
        throw new NotImplementedException();    }

    private String createTelegramCode(long userId){
        return userId+"_"+(int)(new Random().nextDouble()*10000);
    }
}

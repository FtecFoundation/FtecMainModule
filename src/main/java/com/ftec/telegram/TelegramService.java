package com.ftec.telegram;

import com.ftec.entities.TelegramSettings;
import com.ftec.repositories.interfaces.TelegramSettingsDAO;
import com.ftec.resources.TelegramNotifications;
import com.ftec.telegram.exceptions.WrongCodeException;
import com.ftec.utils.SessionHolder;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

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
    public void createCode(){
        createCode(SessionHolder.getCurrentUserId());
    }

    @Transactional
    public String createCode(long userId){
        String code = createTelegramCode(userId);
        telegramSettings.saveCode(userId, code);
        return code;
    }
    @Transactional
    public void disableTelegram(){
        TelegramSettings settings = telegramSettings.get(SessionHolder.getCurrentUserId());
        SendMessage message = new SendMessage();
        message.setText("Telegram account was successfully unlinked");
        message.setChatId(settings.getLinkedUserChatId());
        telegramSettings.deleteCode(SessionHolder.getCurrentUserId());
        try {
            if(settings.getLinkedUserChatId()!=0L)
                SendMessageBot.SendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public TelegramSettings getSettings(){
        return getSettings(SessionHolder.getCurrentUserId());
    }
    @Transactional
    public TelegramSettings getSettings(long userId){
        return telegramSettings.get(userId);
    }
    @Transactional
    public void updateSettings(List<TelegramNotifications> telegramSettings){
        this.telegramSettings.updateNotifications(SessionHolder.getCurrentUserId(), TelegramSettings.convertNotifications(telegramSettings));
    }

    private String createTelegramCode(long userId){
        return userId+"_"+(int)(new Random().nextDouble()*10000);
    }
}

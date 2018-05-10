package com.ftec.services;

import com.ftec.resources.Resources;
import com.ftec.telegram.SendMessageBot;
import com.ftec.telegram.TelegramDispatcher;
import com.ftec.telegram.exceptions.CommandNotFoundException;
import com.ftec.telegram.exceptions.RuWrongStageException;
import com.ftec.telegram.exceptions.WrongStageException;
import com.ftec.telegram.resources.Stages;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;

@Service
public class LifecycleActions {
    private final Resources resources;
    private final TelegramDispatcher telegramDispatcher;

    public LifecycleActions(Resources resources, TelegramDispatcher telegramDispatcher) {
        this.resources = resources;
        this.telegramDispatcher = telegramDispatcher;
    }

    @PostConstruct
    public void init() throws TelegramApiRequestException {
        SendMessageBot.currentToken = resources.getBotToken();
        if(resources.isWebhookUsed()) return;
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        api.registerBot(new TelegramLongPollingBot() {
            @Override
            public String getBotToken() {
                return resources.getBotToken();
            }

            @Override
            public void onUpdateReceived(Update update) {
                try {
                    execute(telegramDispatcher.processQuery(update));
                } catch (CommandNotFoundException e) {
                    e.printStackTrace();
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Command wasn't found.");
                    sendMessage.setChatId(update.getMessage().getChatId());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e1) {
                        e1.printStackTrace();
                    }
                } catch (InvocationTargetException e){
                    e.printStackTrace();
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Error "+e.getCause().getMessage());
                    if(e.getCause().getClass().equals(WrongStageException.class)) sendMessage.setReplyMarkup(Stages.MAIN.getKeyboardMarkup("en"));
                    if(e.getCause().getClass().equals(RuWrongStageException.class)) sendMessage.setReplyMarkup(Stages.MAIN.getKeyboardMarkup("ru"));
                    sendMessage.setChatId(update.getMessage().getChatId());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e1) {
                        e1.printStackTrace();
                    }
                } catch (IllegalAccessException e){
                    e.printStackTrace();
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Unexpected error occurred. "+e.getMessage());
                    sendMessage.setChatId(update.getMessage().getChatId());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e1) {
                        e1.printStackTrace();
                    }
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String getBotUsername() {
                return resources.getBotUsername();
            }
        });
    }

    @PreDestroy
    public void destroy(){

    }
}

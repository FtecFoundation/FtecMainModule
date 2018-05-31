package com.ftec.modules.telegram;

import com.ftec.modules.telegram.resources.Stages;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class SendMessageBot{
    public static String currentToken;
    public static final AbsSender sender = new Sender(new DefaultBotOptions());

    public static void SendMessage(SendMessage message) throws TelegramApiException {
        sender.execute(message);
    }

    public static ReplyKeyboardMarkup getKeyboardMarkup(Stages stage, String locale){
        return stage.getKeyboardMarkup(locale);
    }

    public static void sendLoggedIn(long chatId, String device){
        try {
            SendMessage message = new SendMessage();
            message.setText("Someone just logged in your account from " + device);
            message.setChatId(chatId);
            SendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void sendSocialNotification(long chatId, String url){
        try {
            SendMessage message = new SendMessage();
            message.setText("#SocialAssistant\n\uD83D\uDCECNew message: "+url);
            message.setChatId(chatId);
            SendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void sendBalanceRefilled(long chatId, double amount){
        try {
            SendMessage message = new SendMessage();
            message.setText("Account balance was successfully refilled! Current balance: "+amount);
            message.setChatId(chatId);
            SendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class Sender extends DefaultAbsSender{

        Sender(DefaultBotOptions options) {
            super(options);
        }

        @Override
        public String getBotToken() {
            return currentToken;
        }
    }
}

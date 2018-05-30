package com.ftec.services;

import org.springframework.stereotype.Service;

@Service
public class LifecycleActions {
//    private final Resources resources;
//    private final TelegramDispatcher telegramDispatcher;
//
//    public LifecycleActions(Resources resources, TelegramDispatcher telegramDispatcher) {
//        this.resources = resources;
//        this.telegramDispatcher = telegramDispatcher;
//    }
//
//    @PostConstruct
//    public void init() throws TelegramApiRequestException {
//        SendMessageBot.currentToken = resources.getBotToken();
//        if(resources.isWebhookUsed()) return;
//        ApiContextInitializer.init();
//        TelegramBotsApi api = new TelegramBotsApi();
//        api.registerBot(new TelegramLongPollingBot() {
//            @Override
//            public String getBotToken() {
//                return resources.getBotToken();
//            }
//
//            @Override
//            public void onUpdateReceived(Update update) {
//                try {
//                    execute(telegramDispatcher.processQuery(update));
//                } catch (CommandNotFoundException e) {
//                    e.printStackTrace();
//                    SendMessage sendMessage = new SendMessage();
//                    sendMessage.setText("Command wasn't found.");
//                    sendMessage.setChatId(update.getMessage().getChatId());
//                    try {
//                        execute(sendMessage);
//                    } catch (TelegramApiException e1) {
//                        e1.printStackTrace();
//                    }
//                } catch (InvocationTargetException e){
//                    e.printStackTrace();
//                    SendMessage sendMessage = new SendMessage();
//                    sendMessage.setText("Error "+e.getCause().getMessage());
//                    if(e.getCause().getClass().equals(WrongStageException.class)) sendMessage.setReplyMarkup(Stages.MAIN.getKeyboardMarkup("en"));
//                    if(e.getCause().getClass().equals(RuWrongStageException.class)) sendMessage.setReplyMarkup(Stages.MAIN.getKeyboardMarkup("ru"));
//                    sendMessage.setChatId(update.getMessage().getChatId());
//                    try {
//                        execute(sendMessage);
//                    } catch (TelegramApiException e1) {
//                        e1.printStackTrace();
//                    }
//                } catch (IllegalAccessException e){
//                    e.printStackTrace();
//                    SendMessage sendMessage = new SendMessage();
//                    sendMessage.setText("Unexpected error occurred. "+e.getMessage());
//                    sendMessage.setChatId(update.getMessage().getChatId());
//                    try {
//                        execute(sendMessage);
//                    } catch (TelegramApiException e1) {
//                        e1.printStackTrace();
//                    }
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public String getBotUsername() {
//                return resources.getBotUsername();
//            }
//        });
//    }
//
//    @PreDestroy
//    public void destroy(){
//
//    }
}

package com.ftec.modules.telegram.controllers;

import org.springframework.stereotype.Component;

import com.ftec.modules.telegram.decorators.TelegramController;

@TelegramController
@Component
public class MainController {
//    private final MessageSource messageSource;
//    private final TelegramMainService service;
//    private final TelegramSessionService telegramSessionService;
//    private final TelegramService telegramService;
//
//    public MainController(MessageSource messageSource, TelegramMainService telegramMainService, TelegramSessionService telegramSessionService, TelegramService telegramService) {
//        this.messageSource = messageSource;
//        this.service = telegramMainService;
//        this.telegramSessionService = telegramSessionService;
//        this.telegramService = telegramService;
//    }
//
//    @BotPath({balanceKeyboard, balanceKeyboard_ru})
//    public BotApiMethod getBalance(Update update) throws BotNotRegisteredException {
//        SendMessage message = new SendMessage();
//        message.setChatId(update.getMessage().getChatId());
//        message.setText(messageSource.getMessage("telegram.balance.show", new String[]{"" + service.getCurrentUserBalance(telegramSessionService.getUserId(update))}, new Locale(telegramSessionService.getCurrentLocale(update))));
//        return message;
//    }
//
//    @BotPath({botsModule, botsModule_ru})
//    public BotApiMethod inDevelopment(Update update) throws BotNotRegisteredException {
//        SendMessage message = new SendMessage();
//        message.setChatId(update.getMessage().getChatId());
//        message.setText(messageSource.getMessage("telegram.error.inDevelopment", new String[]{}, new Locale(telegramSessionService.getCurrentLocale(update))));
//        return message;
//    }
//
//
//    @BotPath({"/updatekeyboard"})
//    public BotApiMethod showKeyboard(Update update){
//        SendMessage message = new SendMessage();
//        String locale = telegramSessionService.getCurrentLocale(update);
//        message.setChatId(update.getMessage().getChatId());
//        message.setText(messageSource.getMessage("telegram.keyboard.updated", new String[]{}, new Locale(locale)));
//        message.setReplyMarkup(telegramSessionService.getCurrentStage(update).getKeyboardMarkup(locale));
//        return message;
//    }
//
//    @BotPath({"/changelanguage", changeLanguage, changeLanguage_ru})
//    public BotApiMethod showLanguages(Update update){
//        SendMessage answer = new SendMessage();
//        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
//        List<InlineKeyboardButton> firstRow = new ArrayList<>();
//        InlineKeyboardButton en = new InlineKeyboardButton();
//        en.setText("\uD83C\uDDFA\uD83C\uDDF8 English");
//        en.setCallbackData("/changeLanguageTo en");
//        InlineKeyboardButton ru = new InlineKeyboardButton();
//        ru.setText("\uD83C\uDDF7\uD83C\uDDFA Русский");
//        ru.setCallbackData("/changeLanguageTo ru");
//        firstRow.add(en);
//        firstRow.add(ru);
//        keyboardMarkup.setKeyboard(new ArrayList<List<InlineKeyboardButton>>(){{add(firstRow);}});
//        answer.setReplyMarkup(keyboardMarkup);
//        answer.setChatId(update.getMessage().getChatId());
//        String locale = telegramSessionService.getCurrentLocale(update);
//        answer.setText(messageSource.getMessage("telegram.main.changeLanguage",new String[]{} , new Locale(locale)));
//        return answer;
//    }
//    @BotPath({"/changeLanguageTo"})
//    public BotApiMethod setLanguage(Update update) throws TelegramApiException {
//        String newLocale = update.getCallbackQuery().getData().split(" ")[1];
//        telegramSessionService.setCurrentLocale(update.getCallbackQuery().getMessage().getChatId(), newLocale);
//        AnswerCallbackQuery answer = new AnswerCallbackQuery();
//        answer.setCallbackQueryId(update.getCallbackQuery().getId());
//        answer.setText("Success");
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setReplyMarkup(telegramSessionService.getCurrentStage(update.getCallbackQuery().getMessage().getChatId()).getKeyboardMarkup(newLocale));
//        sendMessage.setText(messageSource.getMessage("telegram.main.languageChanged",new String[]{newLocale} , new Locale(newLocale)));
//        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
//        SendMessageBot.SendMessage(sendMessage);
//        return answer;
//    }
//
//    @BotPath("/supportRequest")
//    public BotApiMethod sendSupportRequest(){
//        return null;
//    }
//
//    @BotPath("/start")
//    public BotApiMethod registerBot(Update update) throws WrongCodeException {
//        if(update.getMessage().getText().split(" ").length!=2) throw new WrongCodeException(messageSource.getMessage("telegram.start.noCode", new String[]{}, new Locale("en")));
//        String param = update.getMessage().getText().split(" ")[1];
//        if(param.split("_").length!=2) throw new WrongCodeException(messageSource.getMessage("telegram.start.wrongCode", new String[]{}, new Locale("en")));
//        long userId=Long.parseLong(param.split("_")[0]);
//        telegramService.linkChatWithUser(userId, update.getMessage().getFrom().getId(), update.getMessage().getFrom().getUserName(), param);
//        String locale = service.getCurrentLocale(userId);
//        SendMessage message = new SendMessage();
//        message.setChatId(update.getMessage().getChatId());
//        telegramSessionService.updateLocale(telegramSessionService.getKey(update), locale);
//        message.setReplyMarkup(SendMessageBot.getKeyboardMarkup(Stages.MAIN, locale));
//        message.setText(messageSource.getMessage("telegram.start.success", new String[]{}, new Locale(locale)));
//        return message;
//    }
//    @BotPath({menu_back, menu_back_ru})
//    public BotApiMethod back(Update update){
//        SendMessage message = new SendMessage();
//        message.setChatId(update.getMessage().getChatId());
//        Stages neededStage = telegramSessionService.getCurrentStage(update).previousStage;
//        if(neededStage == null) neededStage = Stages.MAIN;
//        telegramSessionService.setCurrentStage(update, neededStage);
//        String locale = telegramSessionService.getCurrentLocale(update);
//        message.setText(messageSource.getMessage(neededStage.stageText, new String[]{}, new Locale(locale)));
//        message.setReplyMarkup(SendMessageBot.getKeyboardMarkup(neededStage, locale));
//        return message;
//    }
}

package com.ftec.modules.telegram;

import org.springframework.stereotype.Service;

@Service
public class TelegramDispatcher {
//    private Map<String, Method> botPaths;
//    private Map<Class, Object> controllers;
//
//    private final TelegramSessionService telegramSessionService;
//
//    @Autowired
//    public TelegramDispatcher(MainController mainController, TelegramSessionService telegramSessionService) {
//        this.telegramSessionService = telegramSessionService;
//        botPaths=new HashMap<>();
//        controllers = new HashMap<>();
//
//        List<Object> telegramControllers = new ArrayList<Object>(){{add(mainController);}};
//        for(Object controller: telegramControllers) {
//            controllers.put(controller.getClass(), controller);
//            for (Method method : controller.getClass().getMethods()) {
//                if (method.getAnnotation(BotPath.class) != null) {
//                    for (String path : method.getAnnotation(BotPath.class).value()) {
//                        botPaths.put(path, method);
//                    }
//                }
//            }
//        }
//    }
//
//    public BotApiMethod<? extends Serializable> processQuery(Update update) throws CommandNotFoundException, InvocationTargetException, IllegalAccessException {
//        String path = (update.getMessage()!=null)?update.getMessage().getText().split(" ")[0]:update.getCallbackQuery().getData().split(" ")[0];
//        Method requestMethod = botPaths.get(path);
//        if(requestMethod==null) requestMethod = processInput(update);
//        return (BotApiMethod<? extends Serializable>) requestMethod.invoke(controllers.get(requestMethod.getDeclaringClass()), update);
//    }
//
//    private Method processInput(Update update) throws CommandNotFoundException {
//        JSONObject session = telegramSessionService.getSession(update.getMessage().getFrom().getId());
//        if(!session.has(telegramSessionService.sessionInputName) || !session.has(telegramSessionService.sessionNextMethod)) throw new CommandNotFoundException();
//        String paramToSetName = session.getString(telegramSessionService.sessionInputName);
//        session.put(paramToSetName, update.getMessage().getText());
//        return (Method) session.get(telegramSessionService.sessionNextMethod);
//    }
}

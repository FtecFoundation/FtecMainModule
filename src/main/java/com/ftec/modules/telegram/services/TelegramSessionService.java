package com.ftec.modules.telegram.services;

import org.springframework.stereotype.Service;

@Service
public class TelegramSessionService {
//    public final String sessionInputName = "paramToSet";
//    public final String sessionNextMethod = "nextMethod";
//    public final String sessionStage = "stage";
//    private final String sessionLocale = "locale";
//
//    private final MessageSource messageSource;
//    private final UserDAO userDAO;
//    private static JSONObject session;
//
//    @Autowired
//    public TelegramSessionService(MessageSource messageSource, UserDAO userDAO) {
//        this.messageSource = messageSource;
//        this.userDAO = userDAO;
//        session=new JSONObject();
//    }
//
//    public long getUserId(Update update){
//        JSONObject session = getSession(update);
//        if(!session.has("userId")){
//            session.put("userId", getUserIdByChatId(update));
//        }
//        return session.getLong("userId");
//    }
//    public long getUserId(long chatId){
//        JSONObject session = getSession(chatId);
//        if(!session.has("userId")){
//            session.put("userId", getUserIdByChatId(chatId));
//        }
//        return session.getLong("userId");
//    }
//
//    private long getUserIdByChatId(Update update){
//        //TODO migrate to elasticsearch
//        return 0;
////        return userDAO.getUserByChatId(update.getMessage().getChatId()).getId();
//    }
//    private long getUserIdByChatId(long chatId){
//        //TODO migrate to elasticsearch
//        return 0;
////        return userDAO.getUserByChatId(chatId).getId();
//    }
//    public void setCurrentLocale(Update update, String locale){
//        setCurrentLocale(getKey(update), locale);
//    }
//
//    public void setCurrentLocale(long chatId, String locale){
//        getSession(chatId).put(sessionLocale, locale);
//    }
//
//    public String getCurrentLocale(Update update){
//        return getCurrentLocale(getKey(update));
//    }
//
//    public String getCurrentLocale(long chatId){
//        JSONObject session = getSession(chatId);
//        if(!session.has(sessionLocale)) session.put(sessionLocale, "en");
//        return session.getString(sessionLocale);
//    }
//
//    public void updateLocale(long chatId, String newLocale){
//        getSession(chatId).put(sessionLocale, newLocale);
//    }
//
//    public JSONObject getSession(Update update){
//        return getSession(update.getMessage().getChatId());
//    }
//
//    public long getKey(Update update){
//        return update.getMessage().getChatId();
//    }
//
//    public JSONObject getSession(long chatId){
//        JSONObject ret = null;
//        if(!session.has(""+chatId)){
//            ret = new JSONObject();
//            session.put(""+chatId, ret);
//        }
//        ret = session.getJSONObject(""+chatId);
//        return ret;
//    }
//    public Stages getCurrentStage(Update update) {
//        return getCurrentStage(getKey(update));
//    }
//    public Stages getCurrentStage(long chatId){
//        JSONObject currentSession = getSession(chatId);
//        if(!currentSession.has("stage")){
//            currentSession.put("stage", Stages.MAIN.toString());
//            return Stages.MAIN;
//        }
//        return Stages.valueOf(currentSession.getString("stage"));
//    }
//
//    public void checkCurrentStage(Stages neededStage, Update update) throws WrongStageException {
//        String locale = getCurrentLocale(update.getMessage().getFrom().getId());
//        if(!getCurrentStage(getKey(update)).equals(neededStage)) throw new WrongStageException(messageSource.getMessage("telegram.error.wrongStage", new String[]{neededStage.toString()}, new Locale(locale)));
//    }
//
//    public void setInputVariables(long chatId, String paramName, Method method){
//        JSONObject userSession = getSession(chatId);
//        userSession.put(sessionNextMethod, method); userSession.put(sessionInputName, paramName);userSession.remove(paramName);
//    }
//
//    public void clearInputVariables(long chatId, String paramName){
//        JSONObject userSession = getSession(chatId);
//        userSession.remove(sessionNextMethod); userSession.remove(sessionInputName);userSession.remove(paramName);
//    }
//
//    @PostConstruct
//    public static void loadSession(){
//        String sessionStr = FileUtils.loadAsString("/BitBotFiles/TelegramSession/session.json");
//        if(!sessionStr.isEmpty())
//			try {
//				session = new JSONObject(sessionStr);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    }
//
//    @PreDestroy
//    public static void saveSession(){
//        FileUtils.writeFile("/BitBotFiles/TelegramSession/session.json", session.toString());
//    }
//
//    public void setCurrentStage(Update update, Stages stage){
//        setCurrentStage(getKey(update), stage);
//    }
//    public void setCurrentStage(long userId, Stages stage){
//        getSession(userId).put("stage", stage.toString());
//    }
}

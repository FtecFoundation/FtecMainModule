package com.ftec.services;

import com.ftec.resources.Resources;
import com.ftec.resources.Stocks;
import com.ftec.utils.Logger;
import com.ftec.utils.local_sendpulse.restapi.Sendpulse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MailService  {
    private final MessageSource messageSource;
    private Resources mailRes;
    private final Sendpulse sendpulse;

    @Autowired
    public MailService(MessageSource messageSource, Resources mailRes) {
        this.messageSource = messageSource;
        this.mailRes = mailRes;
        sendpulse = new Sendpulse(mailRes.getUserId(), mailRes.getUserSecret());
    }

    public void sendEmail(List<? extends MailService.EmailUser> users, MailService.Emails emailType){
        if(mailRes.isEmulateEmail()) return;
        try {
            List<Locale> uniqueLocales = new ArrayList<>();
            users.forEach(emailUser -> {
                if (!uniqueLocales.contains(emailUser.language)) uniqueLocales.add(emailUser.language);
            });

            String fromEmail = mailRes.getSendFrom();

            Map<String, Object> from = new HashMap<String, Object>();
            from.put("name", "admin");
            from.put("email", fromEmail);

            Map<Locale, String> subjects = prepareSubjects(uniqueLocales, emailType);
            Map<Locale, String> headers = prepareHeaders(uniqueLocales);
            Map<Locale, String> footers = prepareFooters(uniqueLocales);
            Map<Locale, String> templates = prepareTemplates(uniqueLocales, emailType);

            for (MailService.EmailUser user : users) {

                if (!user.subscribedToEmail) continue;

                try {
                    sendOneEmail(sendpulse, from, subjects.get(user.language), headers.get(user.language), footers.get(user.language), insertValues(templates.get(user.language), user.createParams()), user);
                }catch (Exception e){
                    Logger.logException("While sending email "+emailType.name()+" to user "+user.email, e, true);
                }
            }
        }catch (Exception e){
            Logger.logException("While creating email "+emailType.name(), e, true);
        }
    }

    private void sendOneEmail(Sendpulse sendpulse, Map<String, Object> from, String subject, String header, String footer, String template, MailService.EmailUser user) {
        Map<String, Object> emaildata = new HashMap<String, Object>();

        List<Map> to =  new ArrayList<Map>();

        Map<String,Object> receiver = new HashMap<String,Object>();
        receiver.put("name", user.email);
        receiver.put("email", user.email);

        to.add(receiver);

        emaildata.put("html",header+template+ footer);
        emaildata.put("text", template);
        emaildata.put("subject",subject);
        emaildata.put("from",from);//ok
        emaildata.put("to",to);
        Map<String, Object> result = sendpulse.smtpSendMail(emaildata);
        System.out.println("Results: " + result);
    }

    public void sendToMany(List<String> emails, String subject, String text){
        if(mailRes.isEmulateEmail()) return;

        String sendFromEmail = mailRes.getSendFrom();
        String userId = mailRes.getUserId();
        String userSecret = mailRes.getUserSecret();

        Sendpulse sendpulse = new Sendpulse(userId, userSecret);
        ArrayList<Map> to = new ArrayList<>();

        Map<String, Object> from = new HashMap<String, Object>();
        from.put("name", "admin");
        from.put("email", sendFromEmail);


        for(String email : emails) {
            Map<String, String> elemTo = new HashMap<>();
            elemTo.put("name", email);
            elemTo.put("email", email);
            to.add(elemTo);
        }

        Map<String, Object> emailData = new HashMap<String, Object>();
        emailData.put("html",text);
        emailData.put("text",text);
        emailData.put("subject", subject);
        emailData.put("from",from);//ok
        emailData.put("to",to);

        Map<String, Object> result = sendpulse.smtpSendMail(emailData);
        System.out.println("Results: " + result);
    }

    public static abstract class EmailUser{
        String email;
        boolean subscribedToEmail;
        Locale language;

        private EmailUser(String email, boolean subscribedToEmail, Locale language) {
            this.email = email;
            this.subscribedToEmail = subscribedToEmail;
            this.language = language;
        }

        abstract Map<String, String> createParams();
    }

    public static class Email_BotTradesUser extends MailService.EmailUser {
        String pair;
        String login;
        Stocks stock;

        public Email_BotTradesUser(String email, boolean subscribedToEmail, Locale language, String pair, String login, Stocks stock) {
            super(email, subscribedToEmail, language);
            this.pair = pair;
            this.login = login;
            this.stock = stock;
        }
        public Email_BotTradesUser(String email, boolean subscribedToEmail, String language, String pair, String login, Stocks stock) {
            super(email, subscribedToEmail, new Locale(language));
            this.pair = pair;
            this.login = login;
            this.stock = stock;
        }

        @Override
        Map<String, String> createParams(){
            Map<String, String> params = new HashMap<>();
            params.put("$Login$", login);
            params.put("$Pair$", pair);
            params.put("$Stock$", stock.name());
            return params;
        }

    }
    public static class Email_TradeMissed extends MailService.EmailUser {
        private String login;
        private Stocks stock;

        public Email_TradeMissed(String email, boolean subscribedToEmail, Locale language, String login, Stocks stock) {
            super(email, subscribedToEmail, language);
            this.login = login;
            this.stock = stock;
        }
        public Email_TradeMissed(String email, boolean subscribedToEmail, String language, String login, Stocks stock) {
            super(email, subscribedToEmail, new Locale(language));
            this.login = login;
            this.stock = stock;
        }

        @Override
        Map<String, String> createParams() {
            Map<String, String> params = new HashMap<>();
            params.put("$Login$", login);
            params.put("$Stock$", stock.name());
            return params;
        }
    }
    public static class Email_UsernameOnly extends MailService.EmailUser {
        String login;

        public Email_UsernameOnly(String email, boolean subscribedToEmail, Locale language, String login) {
            super(email, subscribedToEmail, language);
            this.login = login;
        }
        public Email_UsernameOnly(String email, boolean subscribedToEmail, String language, String login) {
            super(email, subscribedToEmail, new Locale(language));
            this.login = login;
        }

        @Override
        Map<String, String> createParams() {
            Map<String, String> params = new HashMap<>();
            params.put("$Login$", login);
            return params;
        }

        @Override
        public String toString() {
            return "Email_UsernameOnly{" +
                    "email='" + email + '\'' +
                    ", subscribedToEmail=" + subscribedToEmail +
                    ", language=" + language +
                    ", login='" + login + '\'' +
                    '}';
        }
    }

    public static class Email_Balance extends MailService.EmailUser {
        String login;
        public double currentBalance;

        public Email_Balance(String email, boolean subscribedToEmail, Locale language, String login, double currentBalance) {
            super(email, subscribedToEmail, language);
            this.login = login;
            this.currentBalance = currentBalance;
        }
        public Email_Balance(String email, boolean subscribedToEmail, String language, String login, double currentBalance) {
            super(email, subscribedToEmail, new Locale(language));
            this.login = login;
            this.currentBalance = currentBalance;
        }

        @Override
        Map<String, String> createParams() {
            Map<String, String> params = new HashMap<>();
            params.put("$Login$", login);
            params.put("$CurrentBalance$", ""+currentBalance);
            return params;
        }
    }
    public static class Email_BotDisabled extends MailService.EmailUser {
        private String login;
        private Stocks stock;

        public Email_BotDisabled(String email, boolean subscribedToEmail, Locale language, String login, Stocks stock) {
            super(email, subscribedToEmail, language);
            this.login = login;
            this.stock = stock;
        }
        public Email_BotDisabled(String email, boolean subscribedToEmail, String language, String login, Stocks stock) {
            super(email, subscribedToEmail, new Locale(language));
            this.login = login;
            this.stock = stock;
        }

        @Override
        Map<String, String> createParams() {
            Map<String, String> params = new HashMap<>();
            params.put("$Login$", login);
            params.put("$Stock$", stock.name());
            return params;
        }
    }

    public static class Email_Link extends MailService.EmailUser {
        String link;
        String linkName;
        String login;

        public Email_Link(String email, boolean subscribedToEmail, Locale language, String link, String linkName, String login) {
            super(email, subscribedToEmail, language);
            this.link = link;
            this.linkName = linkName;
            this.login = login;
        }
        public Email_Link(String email, boolean subscribedToEmail, String language, String link, String linkName, String login) {
            super(email, subscribedToEmail, new Locale(language));
            this.link = link;
            this.linkName = linkName;
            this.login = login;
        }

        @Override
        Map<String, String> createParams() {
            Map<String, String> params = new HashMap<>();
            params.put("$Login$", login);
            params.put("$Link$", link);
            params.put("$LinkName$", linkName);
            return params;
        }
    }
    private String createInfoBody(Locale locale, String text) {
        String answer = prepareTemplates(locale, MailService.Emails.InfoEmail);
        answer = answer.replace("$Text$", text);
        return answer;
    }

    private Map<Locale, String> prepareHeaders(List<Locale> uniqueLocales){
        String prefixPathToHeader = "static/emails/shared/header";
        Map<Locale, String> headers = new HashMap<>();
        for(Locale locale:uniqueLocales){
            headers.put(locale, loadFile(prefixPathToHeader+"_"+locale.getLanguage()+".html"));
        }
        return headers;
    }
    private String prepareHeaders(Locale locale){
        String prefixPathToHeader ="static/emails/shared/header";
        return loadFile(prefixPathToHeader+"_"+locale.getLanguage()+".html");
    }

    private String insertValues(String origin, Map<String, String> params){
        String answer= origin;
        for(String key:params.keySet()){
            answer = answer.replace(key, params.get(key));
        }
        return (params.size()==0)?origin:answer;
    }

    private Map<Locale, String> prepareFooters(List<Locale> uniqueLocales){
        String prefixPathToHeader = "static/emails/shared/footer";
        Map<Locale, String> headers = new HashMap<>();
        for(Locale locale:uniqueLocales){
            headers.put(locale, loadFile(prefixPathToHeader+"_"+locale.getLanguage()+".html"));
        }
        return headers;
    }

    private String prepareFooters(Locale locale){
        String prefixPathToHeader = "static/emails/shared/footer";
        return loadFile(prefixPathToHeader+"_"+locale.getLanguage()+".html");
    }

    private Map<Locale, String> prepareTemplates(List<Locale> uniqueLocales, MailService.Emails emailType){
        Map<Locale, String> templates = new HashMap<>();
        for(Locale locale:uniqueLocales){
            templates.put(locale, loadFile(emailType.getPath()+"_"+locale.getLanguage()+".html"));
        }
        return templates;
    }
    private String prepareTemplates(Locale locale, MailService.Emails emailType){
        return loadFile(emailType.getPath()+"_"+locale.getLanguage()+".html");
    }

    private Map<Locale, String> prepareSubjects(List<Locale> uniqueLocales, MailService.Emails emailType){
        Map<Locale, String> themes = new HashMap<>();
        for(Locale locale:uniqueLocales){
            themes.put(locale, messageSource.getMessage("letters."+emailType.filePrefix+".subject", new String[]{}, locale));
        }
        return themes;
    }
    private String prepareSubjects(Locale locale, MailService.Emails emailType){
        return messageSource.getMessage("letters."+emailType.filePrefix+".subject", new String[]{}, locale);
    }

    private String loadFile(String filename){
        String fallbackLanguage="en";
        try {
            ClassPathResource file = new ClassPathResource(filename);
            if(!file.exists()) file = new ClassPathResource(filename.substring(0, filename.indexOf("_"))+"_"+fallbackLanguage);
            return Files.lines(Paths.get(file.getURI())).collect(Collectors.joining());
        }catch (Exception e){
            Logger.logException("Loading html content from file "+filename, e, true);
        }
        return null;
    }

    /**
     * Sends simple message that supports html without any styles; Usually for admin or messages before styling in html
     * @param to email address of receiver
     * @param subject subject of letter
     * @param text letter message
     */
    public void sendSimpleMessageWithText(String to, String subject, String text) {
        if(mailRes.isEmulateEmail()) return;
        try {
            Map<String, Object> emaildata = new HashMap<String, Object>();

            List<Map> toArr =  new ArrayList<Map>();

            Map<String,Object> receiver = new HashMap<String,Object>();
            receiver.put("name", to);
            receiver.put("email", to);

            toArr.add(receiver);

            emaildata.put("html",text);
            emaildata.put("text", text);
            emaildata.put("subject",subject);
            Map<String, Object> from = new HashMap<String, Object>();
            from.put("name", mailRes.getSendFrom());
            from.put("email", mailRes.getSendFrom());
            emaildata.put("from",from);
            emaildata.put("to",toArr);
            Map<String, Object> result = sendpulse.smtpSendMail(emaildata);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Temporary disable unused messages
    public enum Emails{
        BotDisabled(MailService.Email_BotDisabled.class, "BotDisabled"),
        ManualTrades(MailService.EmailUser.class, "ManualTrades"),
        AutomaticTradesStarted(MailService.Email_BotTradesUser.class, "AutomaticStarted"),
        AutomaticTradesFinished(MailService.Email_BotTradesUser.class, "AutomaticFinished"),
        TrialEnded(MailService.Email_UsernameOnly.class, "TrialEnded"),
        ForgotPassword(MailService.Email_Link.class, "ForgotPassword"),
        BalanceRefilled(MailService.Email_Balance.class, "BalanceRefilled"),
        TradesMissed(MailService.Email_TradeMissed.class, "TradesMissed"),
        SocialAssistant(MailService.Email_UsernameOnly.class, "SocialAssistant"),
        InfoEmail(null, "InfoTemplate");

        private String filePrefix;

        //Class only for convenience of which array to pass in sendEmails function
        Emails(Class userType, String filePrefix){
            this.filePrefix = filePrefix;
        }
        String getPath(){
            return "static/emails/" +filePrefix+"/main";
        }
    }

}

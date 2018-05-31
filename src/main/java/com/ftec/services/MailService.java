package com.ftec.services;

import com.ftec.utils.Logger;
import com.ftec.resources.Resources;
import com.ftec.resources.Stocks;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MailService {

    //For translations
    private final MessageSource messageSource;
    private final Resources resources;

    public MailService(MessageSource messageSource, Resources resources) {
        this.messageSource = messageSource;
        this.resources = resources;
    }

    public void sendEmail(List<? extends EmailUser> users, Emails emailType){
        if(resources.emulateEmail) return;
        try {
            List<Locale> uniqueLocales = new ArrayList<>();
            users.forEach(emailUser -> {
                if (!uniqueLocales.contains(emailUser.language)) uniqueLocales.add(emailUser.language);
            });
            Map<Locale, String> subjects = prepareSubjects(uniqueLocales, emailType);
            Map<Locale, String> headers = prepareHeaders(uniqueLocales);
            Map<Locale, String> footers = prepareFooters(uniqueLocales);
            Map<Locale, String> templates = prepareTemplates(uniqueLocales, emailType);
            for (EmailUser user : users) {
                HtmlEmail email = getEmail();
                if (!user.subscribedToEmail) continue;
                email.addTo(user.email);
                email.setSubject(subjects.get(user.language));
                try {
                    email.setHtmlMsg(headers.get(user.language)
                            + insertValues(templates.get(user.language), user.createParams())
                            + footers.get(user.language));
                    email.send();
                }catch (Exception e){
                    Logger.logException("While sending email "+emailType.name()+" to user "+user.email, e, true);
                }
            }
        }catch (Exception e){
            Logger.logException("While creating email "+emailType.name(), e, true);
        }
    }

    public void sendToMany(List<String> emails, Locale locale, String text){
        if(resources.emulateEmail) return;
        try {
            String message = prepareHeaders(locale)+createInfoBody(locale, text)+prepareFooters(locale);
            for (int i=emails.size()-1; i>=0; i-=10){
                HtmlEmail email = getEmail();
                email.setSubject(prepareSubjects(locale, Emails.InfoEmail));
                email.setHtmlMsg(message);
                ArrayList<InternetAddress> addresses = new ArrayList<>();
                for(int j=0; j<((i>10)?10:i+1); j++){
                    addresses.add(new InternetAddress(emails.get(i-j)));
                }
                email.setTo(addresses);
                try {
                    email.send();
                }catch (Exception e){
                    Logger.logException("While sending email info email to user", e, true);
                }
            }
        }catch (Exception e){
            Logger.logException("While creating info email", e, true);
        }
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

    public static class Email_BotTradesUser extends EmailUser{
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
    public static class Email_TradeMissed extends EmailUser{
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
    public static class Email_UsernameOnly extends EmailUser{
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
    public static class Email_Balance extends EmailUser{
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
    public static class Email_BotDisabled extends EmailUser{
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
    public static class Email_Link extends EmailUser{
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
        String answer = prepareTemplates(locale, Emails.InfoEmail);
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
        String prefixPathToHeader = "static/emails/shared/header";
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

    private Map<Locale, String> prepareTemplates(List<Locale> uniqueLocales, Emails emailType){
        Map<Locale, String> templates = new HashMap<>();
        for(Locale locale:uniqueLocales){
            templates.put(locale, loadFile(emailType.getPath()+"_"+locale.getLanguage()+".html"));
        }
        return templates;
    }
    private String prepareTemplates(Locale locale, Emails emailType){
        return loadFile(emailType.getPath()+"_"+locale.getLanguage()+".html");
    }

    private Map<Locale, String> prepareSubjects(List<Locale> uniqueLocales, Emails emailType){
        Map<Locale, String> themes = new HashMap<>();
        for(Locale locale:uniqueLocales){
            themes.put(locale, messageSource.getMessage("letters."+emailType.filePrefix+".subject", new String[]{}, locale));
        }
        return themes;
    }
    private String prepareSubjects(Locale locale, Emails emailType){
        return messageSource.getMessage("letters."+emailType.filePrefix+".subject", new String[]{}, locale);
    }

    private String loadFile(String filename){
        String fallbackLanguage="en";
        try {
            ClassPathResource file = new ClassPathResource(filename);
            if(!file.exists()) file=new ClassPathResource(filename.substring(0, filename.indexOf("_"))+"_"+fallbackLanguage+".html");
            return Files.lines(Paths.get(file.getPath()), Charset.forName("utf8")).collect(Collectors.joining());
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
        try {
            HtmlEmail email = getEmail();
            email.setFrom("example@example.com");
            email.addTo(to);
            email.setSubject(subject);
            email.setCharset("utf-8");
            email.setHtmlMsg(text);
            email.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HtmlEmail getEmail(){
        HtmlEmail email = new HtmlEmail();
        email.setHostName(resources.email.host_name);
        email.setSmtpPort(resources.email.smtp_port);
        email.setTLS(resources.email.tls);
        email.setSSL(resources.email.ssl);
        email.setCharset("utf-8");
        if(resources.email.authNeeded) {
            email.setAuthentication(resources.email.mailLogin, resources.email.mailPassword);
        }
        return email;
    }
    //Temporary disable unused messages
    public enum Emails{
//        BotDisabled(Email_BotDisabled.class, "BotDisabled"),
//        ManualTrades(EmailUser.class, "ManualTrades"),
//        AutomaticTradesStarted(Email_BotTradesUser.class, "AutomaticStarted"),
//        AutomaticTradesFinished(Email_BotTradesUser.class, "AutomaticFinished"),
//        TrialEnded(Email_UsernameOnly.class, "TrialEnded"),
//        ForgotPassword(Email_Link.class, "ForgotPassword"),
//        BalanceRefilled(Email_Balance.class, "BalanceRefilled"),
//        TradesMissed(Email_TradeMissed.class, "TradesMissed"),
//        SocialAssistant(Email_UsernameOnly.class, "SocialAssistant"),
        InfoEmail(null, "InfoTemplate");

        private String filePrefix;

        //Class only for convenience of which array to pass in sendEmails function
        Emails(Class userType, String filePrefix){
            this.filePrefix = filePrefix;
        }
        String getPath(){
            return "static/emails/"+filePrefix+"/main";
        }
    }
}

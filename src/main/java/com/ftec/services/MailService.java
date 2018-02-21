package com.ftec.services;

import com.ftec.logger.Logger;
import com.ftec.resources.Resources;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MailService {
    //For translations
    private MessageSource messageSource;
    private Resources resources;
    private Logger logger;

    @Autowired
    public MailService(MessageSource messageSource, Resources resources, Logger logger) {
        this.messageSource = messageSource;
        this.resources = resources;
        this.logger=logger;
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
            email.setFrom(resources.email.fromName);
            email.addTo(to);
            email.setSubject(subject);
            email.setHtmlMsg(text);
            email.send();
        } catch (Exception e) {
            logger.logException("EmailSender","sendSimpleMessageWithText to user "+to, e);
        }
    }

    public void sendHtmlMessage(Emails emailType, Map<String, String> texts, String userEmail, Locale letterLocale){
        try {
            List<File> imageFiles = new ArrayList<>();
            List<String> images = emailType.getImages();
            HtmlEmail email = getEmail();
            email.setFrom(resources.email.fromName);
            email.addTo(userEmail);

            if(images!=null)
                for(String path:images){
                    imageFiles.add(new ClassPathResource("static/img/email/"+path).getFile());
                }
            String emailString = loadFile(emailType.getFilePath());

            for(byte i=0; i<images.size();i++){
                emailString = emailString.replace("%"+images.get(i)+"%", email.embed(imageFiles.get(i)));
            }
            if(texts!=null)
                for(String key:texts.keySet()){
                    emailString = emailString.replaceAll(key, messageSource.getMessage(texts.get(key), new String[]{}, letterLocale));
                }

            email.setHtmlMsg(emailString);
            email.send();
        } catch (Exception e){
            logger.logException("EmailSender", "sendHtmlMessage function, for email "+userEmail+", email type = "+emailType, e);
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

    private String loadFile(String filename){
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(new ClassPathResource("static/emails/" + filename).getURI()));
            return new String(encoded, "UTF-8");
        }catch (Exception e){
            logger.logException("EmailCreator", "loadFile for email "+filename+" encountered error",e);
        }
        return null;
    }

    public enum Emails{
        TestEmail;

        public List<String> getImages(){
            switch (this){
                case TestEmail:
                return TestEmail_images;
            }
            return null;
        }

        public String getFilePath(){
            switch (this){
                case TestEmail:
                    return "";
            }
            return null;
        }
    }

    public final static List<String> TestEmail_images = new ArrayList<String>();

    public static Map<String, String> TestEmail_texts(String login, String link){
        return new HashMap<String, String>(){
            {
                put("$Greetings$","letters.commons.greetings");
                put("$Login$",login);
                put("$Description$","letters.ForgotPassword.description");
                put("$Link$",link);
                put("$Footer$","letters.commons.footer");
            }
        };
    }
}

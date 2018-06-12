package com.ftec.services;

import com.ftec.entities.ConfirmData;
import com.ftec.repositories.ConfirmDataDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.enums.ConfirmScope;
import com.ftec.utils.RandomHashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConfirmEmailService {

    private static final String CONFIRM_URL = "/confirmUrl?hash=";
    private final ConfirmDataDAO confirmDataDAO;
    private final MailService mailService;
    public final static int expiration_time = 172800000;
    private final UserDAO userDAO;

    @Autowired
    public ConfirmEmailService(ConfirmDataDAO confirmDataDAO, MailService mailService, UserDAO userDAO) {
        this.confirmDataDAO = confirmDataDAO;
        this.mailService = mailService;
        this.userDAO = userDAO;
    }

    //should be called while registration
    public void sendConfirmEmailUrl(String email, long userId){
        ConfirmData emailConfirm = ConfirmEmailService.createConfirmData(userId);
        deleteOldEmailHash(userId);
        saveNewHash(emailConfirm);
        sendEmail(email, userId, emailConfirm.getHash());
    }

    private void sendEmail(String email, long userId, String hash) {
        Locale locale = userDAO.findLocaleByEmail(email);


        mailService.sendSimpleMessageWithText(email, "Confirm email", CONFIRM_URL + hash);
    }

    private void saveNewHash(ConfirmData emailConfirm) {
        confirmDataDAO.save(emailConfirm);
    }

    private void deleteOldEmailHash(long userId) {
        confirmDataDAO.deleteByUserIdAndScope(userId, ConfirmScope.ConfirmEmail);
    }

    private static ConfirmData createConfirmData(long userId){
        ConfirmData emailConfirm = new ConfirmData();
        emailConfirm.setHash(RandomHashGenerator.generateRandomString());
        emailConfirm.setUrlExpiredDate(getExpirationDate());
        emailConfirm.setUserId(userId);
        emailConfirm.setScope(ConfirmScope.ConfirmEmail);
        return emailConfirm;
    }

    private static Date getExpirationDate() {
        return new Date(new Date().getTime()+expiration_time);
    }

    public void confirmEmail(String hash){
        Optional<ConfirmData> confirmData = confirmDataDAO.findByHashAndScope(hash, ConfirmScope.ConfirmEmail);
        if(!confirmData.isPresent()) return;//throw exception

        ConfirmData data = confirmData.get();

        //set confirm email to true for user by data.getId()
        //delete by hash
    }
}

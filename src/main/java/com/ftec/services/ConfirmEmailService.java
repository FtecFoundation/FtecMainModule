package com.ftec.services;

import com.ftec.entities.ConfirmData;
import com.ftec.entities.User;
import com.ftec.repositories.ConfirmDataDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.resources.enums.ConfirmScope;
import com.ftec.utils.RandomHashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ConfirmEmailService {

    private static final String CONFIRM_EMAIL = Resources.domainUrlStatic + "/confirmEmail?hash=";
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

    public void sendConfirmEmailUrl(String email, long userId){
        ConfirmData emailConfirm = ConfirmEmailService.createConfirmData(userId);
        deleteOldEmailHash(userId);
        saveNewHash(emailConfirm);
        sendEmail(email, userId, emailConfirm.getHash());
    }

    private void sendEmail(String email, long userId, String hash) {
        Locale locale = userDAO.findLocaleByEmail(email);

        mailService.sendSimpleMessageWithText(email, "Confirm email", CONFIRM_EMAIL + hash);
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
        return new Date(new Date().getTime() + expiration_time);
    }

    @Transactional
    public void confirmEmail(String hash){
        Optional<ConfirmData> confirmData = confirmDataDAO.findByHashAndScope(hash, ConfirmScope.ConfirmEmail);
        if(!confirmData.isPresent()){
            System.out.println("Implement an exception");
            return;//throw exception
        }

        ConfirmData data = confirmData.get();

        User user = userDAO.findById(data.getUserId()).get();
        user.setConfirmedEmail(true);
        userDAO.save(user);

        confirmDataDAO.deleteByHash(hash);
    }

    public Optional<ConfirmData> findByUserIdAndScope(long id, ConfirmScope scope) {
        return confirmDataDAO.findByUserIdAndScope(id, scope);
    }
}

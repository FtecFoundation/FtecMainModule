package com.ftec.services.Implementations;

import com.ftec.constratints.Patterns;
import com.ftec.entities.ConfirmData;
import com.ftec.entities.User;
import com.ftec.exceptions.InvalidHashException;
import com.ftec.exceptions.InvalidUserDataException;
import com.ftec.exceptions.WeakPasswordException;
import com.ftec.repositories.ConfirmDataDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.resources.enums.ConfirmScope;
import com.ftec.services.ConfirmEmailService;
import com.ftec.services.MailService;
import com.ftec.services.interfaces.ConfirmDataService;
import com.ftec.services.interfaces.TokenService;
import com.ftec.utils.PasswordUtils;
import com.ftec.utils.RandomHashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ConfirmDataServiceImpl implements ConfirmDataService {

    final private ConfirmDataDAO confirmDataDAO;
    final private UserDAO userDAO;
    final private MailService mailService;
    final private TokenService tokenService;
    final private ConfirmEmailService confirmEmailService;
    final public static String RESTORE_URL = Resources.domainUrlStatic + "/restorePass?hash=";
    final public static long ULR_EXPIRED_TIME = 7200000;

    @Autowired
    public ConfirmDataServiceImpl(ConfirmDataDAO confirmDataDAO, UserDAO userDAO, MailService mailService, TokenService tokenService, ConfirmEmailService confirmEmailService) {
        this.confirmDataDAO = confirmDataDAO;
        this.userDAO = userDAO;
        this.mailService = mailService;
        this.tokenService = tokenService;
        this.confirmEmailService = confirmEmailService;
    }

    @Transactional
    @Override
    public void sendRestorePassUrl(String data) throws InvalidUserDataException {
        if(userDAO.findByEmail(data).isPresent()){
            sendRestoreUrl(getNewHashForUser(userDAO.findUsernameByEmail(data)), data);
            return;
        }
        if(userDAO.findByUsername(data).isPresent()){
            sendRestoreUrl(getNewHashForUser(data), userDAO.findEmailByUsername(data));
        }
        else throw new InvalidUserDataException("User with this email or login does not exist!");
    }

    private void sendRestoreUrl(String hash, String email) {
        Locale locale = userDAO.findLocaleByEmail(email); // this way or not?

        List<MailService.Email_Link> list = new ArrayList<>() ;

        list.add(new MailService.Email_Link(email, true, locale, RESTORE_URL + hash, "link", userDAO.findUsernameByEmail(email)));
        mailService.sendEmail(list, MailService.Emails.ForgotPassword);
    }

    private String getNewHashForUser(String username) {
        String new_hash = RandomHashGenerator.generateRandomString();
        updateHash(userDAO.findIdByUsername(username), new_hash);
        return new_hash;
    }

    private void updateHash(long userId, String new_hash) {
        if(confirmDataDAO.findById(userId).isPresent()) confirmDataDAO.deleteById(userId);
        confirmDataDAO.save(new ConfirmData(userId,new_hash,getExpiredTime(),ConfirmScope.RestorePass));
    }

    private static Date getExpiredTime() {
        return new Date(new Date().getTime() + ULR_EXPIRED_TIME);
    }

    @Transactional
    @Override
    public void processChangingPass(String hash, String new_pass) throws InvalidHashException, WeakPasswordException {
        verifyHash(hash);
        changePass(confirmDataDAO.findIdByHash(hash),new_pass);
        tokenService.deleteByUserId(confirmDataDAO.findIdByHash(hash));
        confirmDataDAO.deleteByHashAndScope(hash, ConfirmScope.RestorePass);
    }

    @Override
    public Optional<ConfirmData> findById(long id) {
        return confirmDataDAO.findById(id);
    }

    @Override
    public Optional<ConfirmData> findByUserIdAndScope(long id, ConfirmScope scope) {
        return confirmDataDAO.findByUserIdAndScope(id,scope);
    }

    private void changePass(long userId, String new_pass)  throws WeakPasswordException {
        Patterns.validatePass(new_pass);
        User user = userDAO.findById(userId).get();

        user.setPassword(PasswordUtils.generateSecurePassword(new_pass, user.getSalt()));
        userDAO.save(user);
    }

    private void verifyHash(String hash) throws InvalidHashException {
        if(!confirmDataDAO.findByHash(hash).isPresent() || !confirmDataDAO.findByHash(hash).get().getUrlExpiredDate().after(new Date())){
            throw new InvalidHashException("Invalid hash!");
        }
    }

    public void sendConfirmEmail(String email, long userId){
        confirmEmailService.sendConfirmEmailUrl(email,userId);
    }

    public void confirmEmail(String hash){
       confirmEmailService.confirmEmail(hash);
    }
}
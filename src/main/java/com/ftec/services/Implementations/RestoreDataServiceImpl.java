package com.ftec.services.Implementations;

import com.ftec.constratints.Patterns;
import com.ftec.entities.RestoreData;
import com.ftec.entities.User;
import com.ftec.exceptions.RestoreException;
import com.ftec.exceptions.UserNotExistsException;
import com.ftec.repositories.RestoreDataDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.services.MailService;
import com.ftec.services.interfaces.RestoreDataService;
import com.ftec.services.interfaces.TokenService;
import com.ftec.utils.PasswordUtils;
import com.ftec.utils.RandomHashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
public class RestoreDataServiceImpl implements RestoreDataService {

    final private RestoreDataDAO restoreDataDAO;
    final private UserDAO userDAO;
    final private MailService mailService;
    final private TokenService tokenService;
    final public static String RESTORE_URL = Resources.domainUrlStatic + "/restorePass?hash=";
    final public static long ULR_EXPIRED_TIME = 7200000;

    @Autowired
    public RestoreDataServiceImpl(RestoreDataDAO restoreDataDAO, UserDAO userDAO, MailService mailService, TokenService tokenService) {
        this.restoreDataDAO = restoreDataDAO;
        this.userDAO = userDAO;
        this.mailService = mailService;
        this.tokenService = tokenService;
    }

    @Transactional
    @Override
    public void sendRestorePassUrl(String data) throws UserNotExistsException {
        if(userDAO.findByEmail(data).isPresent()){
            sendRestoreUrl(getNewHashForUser(userDAO.findUsernameByEmail(data)), data);
            return;
        }
        if(userDAO.findByUsername(data).isPresent()){
            sendRestoreUrl(getNewHashForUser(data), userDAO.findEmailByUsername(data));
        }
        else throw new UserNotExistsException("User with this email or login does not exist!");
    }

    private void sendRestoreUrl(String hash, String email) {
        Locale locale = userDAO.findLocaleByEmail(email); // this way or not?

        ArrayList<MailService.Email_Link> list = new ArrayList<MailService.Email_Link>() ;

        list.add(new MailService.Email_Link(email, true, locale, RESTORE_URL + hash, "link", userDAO.findUsernameByEmail(email)));
        mailService.sendEmail(list, MailService.Emails.ForgotPassword);
        }

        private String getNewHashForUser(String username) {
        String new_hash = RandomHashGenerator.generateRandomString();
        updateHash(userDAO.findIdByUsername(username), new_hash);
        return new_hash;
    }

    private void updateHash(long userId, String new_hash) {
        if(restoreDataDAO.findById(userId).isPresent()) restoreDataDAO.deleteById(userId);
        restoreDataDAO.save(new RestoreData(userId,new_hash,getExpiredTime()));
    }

    private static Date getExpiredTime() {
        return new Date(new Date().getTime() + ULR_EXPIRED_TIME);
    }

    @Transactional
    @Override
    public void processChangingPass(String hash, String new_pass) throws RestoreException {
        verifyHash(hash);
        changePass(restoreDataDAO.findIdByHash(hash),new_pass);
        tokenService.deleteByUserId(restoreDataDAO.findIdByHash(hash));
        deleteByHash(hash);
    }

    @Override
    public Optional<RestoreData> findById(long id) {
        return restoreDataDAO.findById(id);
    }

    private void changePass(long userId, String new_pass)  throws RestoreException {
        Patterns.validatePass(new_pass);
        User user = userDAO.findById(userId).get();

        user.setPassword(PasswordUtils.generateSecurePassword(new_pass, user.getSalt()));
        userDAO.save(user);
    }

    private void verifyHash(String hash) throws RestoreException {
        if(!restoreDataDAO.findByHash(hash).isPresent() || !restoreDataDAO.findByHash(hash).get().getUrlExpiredDate().after(new Date())){
            throw new RestoreException("Invalid hash!");
        }
    }

    private void deleteByHash(String hash){
        restoreDataDAO.deleteByHash(hash);
    }
}
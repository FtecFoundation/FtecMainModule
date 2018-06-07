package com.ftec.services.Implementations;

import com.ftec.constratints.Patterns;
import com.ftec.entities.RestoreData;
import com.ftec.entities.User;
import com.ftec.exceptions.EmailNotExistException;
import com.ftec.exceptions.UserNotExistsException;
import com.ftec.repositories.RestoreDataDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.services.MailService;
import com.ftec.services.interfaces.RestoreDataService;
import com.ftec.utils.PasswordUtils;
import com.ftec.utils.RandomHashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;

@Service
public class RestoreDataServiceImpl implements RestoreDataService {

    final private RestoreDataDAO restoreDataDAO;
    final private UserDAO userDAO;
    final private MailService mailService;

    final public static String RESTORE_URL = Resources.domainUrlStatic + "/restorePass?hash=";
    final public static long ULR_EXPIRED_TIME = 7200000;

    @Autowired
    public RestoreDataServiceImpl(RestoreDataDAO restoreDataDAO, UserDAO userDAO, MailService mailService) {
        this.restoreDataDAO = restoreDataDAO;
        this.userDAO = userDAO;
        this.mailService = mailService;
    }
    @Override
    public void sendRestorePassUrlByUsername(String username) throws UserNotExistsException {
        //check
        String email = userDAO.findEmailByUsername(username);
        sendRestoreUrl(getRestorePassByUsername(username), email);
    }
    @Override
    public void sendRestorePassUrlByEmail(String email) throws EmailNotExistException {
        sendRestoreUrl(getRestorePassByEmail(email), email);
    }

    private void sendRestoreUrl(String hash, String email) {
        String restoreUrl = RESTORE_URL + hash;
        mailService.sendToMany(new LinkedList<String>(){{add(email);}}, "Restore pass for " + userDAO.findByEmail(email).get().getUsername(), restoreUrl);
    }

    private String getRestorePassByUsername(String username) throws UserNotExistsException {
        Optional<User> userOptional = userDAO.findByUsername(username);

        if(!userOptional.isPresent()) throw new UserNotExistsException();

        else  return getNewUrlForUser(userOptional.get().getId());
    }

    private String getRestorePassByEmail(String email) throws EmailNotExistException{
        Optional<User> userOptional = userDAO.findByEmail(email);

        if(!userOptional.isPresent())  throw new EmailNotExistException();

        else return getNewUrlForUser(userOptional.get().getId());

    }

    private String getNewUrlForUser(long userId) {
        String restoreUrl = generateHash(userId);
        deleteAndSaveUrl(userId, restoreUrl);
        return restoreUrl;
    }

    private void deleteAndSaveUrl(long userId, String restoreUrl) {
        Optional<RestoreData> dataOptional = restoreDataDAO.findById(userId);
        if(dataOptional.isPresent())restoreDataDAO.deleteById(userId);
        restoreDataDAO.save(new RestoreData(userId,restoreUrl,getExpiredTime()));
    }

    private static Date getExpiredTime() {
        return new Date(new Date().getTime() + ULR_EXPIRED_TIME);
    }

    private String generateHash(long userId) {
        return  RandomHashGenerator.generateRandomString();
    }

    @Override
    public void changePass(long userId, String restore_url, String new_pass)  throws IOException {
        Patterns.validatePass(new_pass);
        User user = userDAO.findById(userId).get();

        user.setPassword(PasswordUtils.generateSecurePassword(new_pass, user.getSalt()));
        userDAO.save(user);
    }

    @Override
    public boolean isHashValid(String hash){
        return restoreDataDAO.findByHash(hash).isPresent()
                && restoreDataDAO.findByHash(hash).get().getUrlExpiredDate().after(new Date());
    }
}
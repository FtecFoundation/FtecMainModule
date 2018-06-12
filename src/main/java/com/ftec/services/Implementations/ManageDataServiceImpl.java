package com.ftec.services.Implementations;

import com.ftec.exceptions.InvalidHashException;
import com.ftec.exceptions.InvalidUserDataException;
import com.ftec.exceptions.WeakPasswordException;
import com.ftec.repositories.ConfirmDataDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.services.ConfirmEmailService;
import com.ftec.services.MailService;
import com.ftec.services.PasswordRestoreService;
import com.ftec.services.interfaces.ConfirmDataService;
import com.ftec.services.interfaces.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmDataServiceImpl implements ConfirmDataService {

    final private ConfirmEmailService confirmEmailService;
    final private PasswordRestoreService passwordRestoreService;

    @Autowired
    public ConfirmDataServiceImpl(ConfirmDataDAO confirmDataDAO, UserDAO userDAO, MailService mailService, TokenService tokenService, ConfirmEmailService confirmEmailService, PasswordRestoreService passwordRestoreService) {
        this.confirmEmailService = confirmEmailService;
        this.passwordRestoreService = passwordRestoreService;
    }


    public void sendRestorePassUrl(String data) throws InvalidUserDataException {
        passwordRestoreService.sendRestorePassUrl(data);
    }

    public void processChangingPass(String hash, String new_pass) throws InvalidHashException, WeakPasswordException {
        passwordRestoreService.processChangingPass(hash, new_pass);
    }

    public void sendConfirmEmail(String email, long userId){

        confirmEmailService.sendConfirmEmailUrl(email,userId);
    }

    public void confirmEmail(String hash){
       confirmEmailService.confirmEmail(hash);
    }
}
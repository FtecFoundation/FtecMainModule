package com.ftec.services;

import com.ftec.controllers.RegistrationController;
import com.ftec.repositories.interfaces.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final UserDAO userDAO;

    @Autowired
    public RegistrationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean registerUser(RegistrationController.RegistrationUser user){
        //TODO implement registration
        return true;
    }
}

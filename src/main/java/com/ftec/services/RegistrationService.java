package com.ftec.services;

import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final UserDAO userDAO;

    @Autowired
    public RegistrationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public long registerUser(){
        return 0;
    }

    private void authUser(User u){
    }
}

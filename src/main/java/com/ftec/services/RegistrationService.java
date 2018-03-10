package com.ftec.services;

import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;
import com.ftec.repositories.interfaces.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(RegistrationController.RegistrationUser user){
        User qualifiedUser = new User(user);
        qualifiedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.persist(qualifiedUser);
        //TODO add partners support
        return true;
    }
}

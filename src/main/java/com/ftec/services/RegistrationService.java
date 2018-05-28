package com.ftec.services;

import com.ftec.controllers.RegistrationController;
import com.ftec.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RegistrationService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public long registerUser(RegistrationController.RegistrationUser user){
        User qualifiedUser = new User(user);
        qualifiedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.persist(qualifiedUser);
        authUser(qualifiedUser);
        //TODO add partners support
        return qualifiedUser.getId();
    }

    private void authUser(User u){

    }

    @Transactional
    public User getUser(long userId){
        return userDAO.getById(userId);
    }
    @Transactional
    public boolean deleteUser(long userId){
        return userDAO.deleteUser(userId);
    }
}

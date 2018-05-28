package com.ftec.services;

import com.ftec.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SecurityService {
    private final UserDAO userDAO;

    @Autowired
    public SecurityService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Transactional
    public User getUser(String login){
        return userDAO.getByLogin(login);
    }
}

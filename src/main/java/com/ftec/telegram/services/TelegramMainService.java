package com.ftec.telegram.services;

import com.ftec.repositories.interfaces.UserDAO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TelegramMainService {
    public final UserDAO userDAO;

    public TelegramMainService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    public double getCurrentUserBalance(long userId){
        return userDAO.getUserBalance(userId);
    }

    @Transactional
    public String getCurrentLocale(long userId){
        return userDAO.getLocale(userId);
    }
}

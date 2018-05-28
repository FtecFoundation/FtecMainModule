package com.ftec.telegram.services;

import com.ftec.repositories.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class TelegramMainService {
    public final UserDAO userDAO;

    public TelegramMainService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public double getCurrentUserBalance(long userId){
        // TODO: 5/28/18 Migrate to elasticsearch
        return 0;
//        return userDAO.getUserBalance(userId);
    }

    public String getCurrentLocale(long userId){
        // TODO: 5/28/18 Migrate to elasticsearch 
        return "";
//        return userDAO.getLocale(userId);
    }
}

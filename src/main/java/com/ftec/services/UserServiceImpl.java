package com.ftec.services;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User getById(long id) {
        return userDAO.findById(id);
    }

    @Override
    public User registerNewUserAccount(User newUser) throws UserExistException {
        try {
            return userDAO.save(newUser);
        } catch (Exception e) {
            throw new UserExistException();
        }
    }


}

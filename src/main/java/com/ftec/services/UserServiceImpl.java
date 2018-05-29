package com.ftec.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User getById(long id) {
        return userDAO.findById(id).get();
    }

    @Override
    public void registerNewUserAccount(User user) throws UserExistException {
        try {
            userDAO.save(user);
        } catch (Exception e) {
            List<User> allUsers = userDAO.findAll();
            for (User allUser : allUsers) {
                if (allUser.equals(user)) {
                    throw new UserExistException();
                }
            }
        }
    }
}

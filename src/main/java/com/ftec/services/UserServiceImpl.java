package com.ftec.services;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.UserService;
import com.ftec.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Optional<User> userInDb = userDAO.findByUsername(user.getUsername());
        if (!(userInDb.isPresent())) {
            String userPassword = user.getPassword();
            String salt = PasswordUtils.getSalt(30);
            String securedPassword = PasswordUtils.generateSecurePassword(userPassword, salt);

            user.setPassword(securedPassword);
            userDAO.save(user);
        } else {
            throw new UserExistException();
        }
    }
}

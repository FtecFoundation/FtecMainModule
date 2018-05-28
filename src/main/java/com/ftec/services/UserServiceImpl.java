package com.ftec.services;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.IdManager;
import com.ftec.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final IdManager idManagement;
    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(IdManager idManagement, UserDAO userDAO) {
        this.idManagement = idManagement;
        this.userDAO = userDAO;
    }

    @Override
    public User getById(long id) {
        return userDAO.findById(id);
    }

    @Override
    public User registerNewUserAccount(User user) throws UserExistException {
        try {
            User newUser = new User();
//            newUser.setId();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(user.getPassword());
            newUser.setEmail(user.getEmail());
            return userDAO.save(newUser);
        } catch (Exception e) {
            throw new UserExistException();
        }
    }


}

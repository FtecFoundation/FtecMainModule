package com.ftec.services.Implementations;

import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.UserService;
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
    public Optional<User> getById(long id) {
        return userDAO.findById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }


    /**
     * @param username - users name
     * @return {@code true} if there is a User present, otherwise {@code false}.
     */
    public boolean isDuplicateUserName(String username) {
        Optional<User> userInDb = userDAO.findByUsername(username);

        return userInDb.isPresent();
    }
}
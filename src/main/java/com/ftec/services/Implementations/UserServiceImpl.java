package com.ftec.services.Implementations;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.UserService;
import com.ftec.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public void registerNewUserAccount(User user) throws UserExistException {
        user.setPassword(encodeUserPassword(user.getPassword()));
        userDAO.save(user);
    }

    /**
     * Takes the users password and encodes it into secured
     *
     * @param userPassword - raw Password
     * @return secured password
     */
    private String encodeUserPassword(String userPassword) {
        String salt = PasswordUtils.getSalt(30);
        return PasswordUtils.generateSecurePassword(userPassword, salt);
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


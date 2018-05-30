package com.ftec.services;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.repositories.IdsDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.UserService;
import com.ftec.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final IdsDAO idsDAO;

    public static final String com_ftec_entities_User = "com.ftec.entities.User";

    @Autowired
    public UserServiceImpl(UserDAO userDAO, IdsDAO idsDAO) {
        this.userDAO = userDAO;
        this.idsDAO = idsDAO;
    }

    @Override
    public User getById(long id) {
        return userDAO.findById(id).get();
    }

    @Override
    public void registerNewUserAccount(User user) throws UserExistException {
        if (!isDuplicateUserName(user.getUsername())) {
            String securedPassword = encodeUserPassword(user.getPassword());
            user.setPassword(securedPassword);
            //TODO inrementAndGetLastId(user);  uncomment after fix idsDAO.incrementLastId(...) method
            userDAO.save(user);
        } else {
            throw new UserExistException();
        }
    }

    public void inrementAndGetLastId(User user) {
        idsDAO.incrementLastId(com_ftec_entities_User);
        user.setId(idsDAO.findByTableName(com_ftec_entities_User).getLastId());
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


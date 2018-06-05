package com.ftec.services.Implementations;

import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.PasswordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserDAO userDAO;

    public RegistrationServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static User registerUser(RegistrationController.UserRegistration userRegistration) {
        User userToSave = new User(userRegistration);
        userToSave.fillEmptyFields();
        return userToSave;
    }

    @Transactional
    @Override
    public void registerNewUserAccount(User user) {
        user.setPassword(PasswordUtils.encodeUserPassword(user.getPassword(), user.getSalt()));
        userDAO.save(user);
    }
}

package com.ftec.services.Implementations;

import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.PasswordUtils;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Override
    public User registerUser(RegistrationController.UserRegistration userRegistration) {
        User userToSave = new User(userRegistration);
        userToSave.fillEmptyFields();
        return userToSave;
    }
}

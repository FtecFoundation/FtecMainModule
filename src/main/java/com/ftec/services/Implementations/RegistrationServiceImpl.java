package com.ftec.services.Implementations;

import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;
import com.ftec.services.interfaces.RegistrationService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {



    @Override
    public User registerUser(RegistrationController.UserRegistration userRegistration) {
        User userToSave = new User(userRegistration);
        //todo Put increment method into getLastId

        //todo id
        userToSave.fillEmptyFields();
        return userToSave;
    }
}

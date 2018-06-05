package com.ftec.services.interfaces;

import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;

public interface RegistrationService {
    void registerNewUserAccount(User user) throws UserExistException;

}

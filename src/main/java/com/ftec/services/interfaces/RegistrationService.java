package com.ftec.services.interfaces;

import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;

public interface RegistrationService {
    User registerUser(RegistrationController.UserRegistration userRegistration);
}

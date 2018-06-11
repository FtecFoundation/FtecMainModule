package com.ftec.services.interfaces;

import com.ftec.controllers.LoginController;
import com.ftec.entities.User;
import com.ftec.exceptions.AuthorizationException;

import java.util.Optional;

public interface AuthorizationService {
     String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password!";
     String WRONG_2FA_CODE = "2Fa code is emply!";

    void authorizate(Optional<User> userOpt, LoginController.UserAuth userAuth) throws AuthorizationException;
}

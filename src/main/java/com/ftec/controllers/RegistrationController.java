package com.ftec.controllers;

import com.ftec.constratints.UniqueEmail;
import com.ftec.constratints.UniqueLogin;
import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;

@RestController
public class RegistrationController {

    private final UserService userService;
    private final TokenService tokenService;
    private final RegistrationService registrationService;


    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public MvcResponse createUser(@RequestBody @Valid UserRegistration userRegistration, HttpServletResponse response) throws UserExistException, IOException, IOException {
        try {
            User userToSave = registrationService.registerUser(userRegistration);
            userService.registerNewUserAccount(userToSave);

            String token = tokenService.createSaveAndGetNewToken(userToSave.getId());
            return new MvcResponse(200, "token", token);
        } catch (TokenException e) {
            response.sendError(400);
            return null;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRegistration {

        @NotNull
        @Size(min = 4)
        @UniqueLogin
        private String username;

        @NotNull
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
        private String password;

        @NotNull
        @UniqueEmail
        private String email;

        private boolean subscribeForNews;
    }

    @Autowired
    public RegistrationController(UserService userService, TokenService tokenService, RegistrationService registrationService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.registrationService = registrationService;
    }
}

package com.ftec.controllers;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.resources.MailResources;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final TokenService tokenService;
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(UserService userService, TokenService tokenService, RegistrationService registrationService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.registrationService = registrationService;
    }

    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public MvcResponse createUser(@RequestBody @Valid UserRegistration userRegistration, HttpServletResponse response) throws UserExistException, IOException, IOException {
        try {
            User userToSave = registrationService.registerUser(userRegistration);
            userService.registerNewUserAccount(userToSave);
            long id = userToSave.getId();
            if (id == 0)
                throw new TokenException("Exception while generating user token: User haven't id or id equals to 0!");

            String token = tokenService.createSaveAndGetNewToken(id);
            return new MvcResponse(200, "token", token);
        } catch (TokenException e) {
            response.sendError(400);
            return null;
        }
    }


    @Autowired
    MailResources mailRes;

    public static class UserRegistration {
        private String username;

        private String password;

        private String email;

        private boolean subscribeForNews;

        public UserRegistration() {
        }

        public UserRegistration(String username, String password, String email, boolean subscribeForNews) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.subscribeForNews = subscribeForNews;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isSubscribeForNews() {
            return subscribeForNews;
        }

        public void setSubscribeForNews(boolean subscribeForNews) {
            this.subscribeForNews = subscribeForNews;
        }
    }
}

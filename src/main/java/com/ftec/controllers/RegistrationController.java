package com.ftec.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftec.entities.User;
import com.ftec.exceptions.TokenException;
import com.ftec.exceptions.UserExistException;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.UserService;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final TokenService tokenService;

    private static class UserRegistration {
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


    @Autowired
    public RegistrationController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(path = "/registr_test", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody UserRegistration userRegistration, HttpServletResponse respone) throws UserExistException {
        try {
            User userToSave = new User();
            userToSave.setUsername(userRegistration.getUsername());
            userToSave.setPassword(userRegistration.getPassword());
            userToSave.setEmail(userRegistration.getEmail());
            userToSave.setSubscribeForNews(userRegistration.isSubscribeForNews());
            userToSave.fillEmptyFields();
            userService.registerNewUserAccount(userToSave);
            sendToken(userToSave, respone);

            return new ResponseEntity<String>(HttpStatus.CREATED);
        } catch (TokenException e) {

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public void sendToken(User user, HttpServletResponse respone) throws TokenException {

        long id = user.getId();
        if (id == 0)
            throw new TokenException("Exception while generating user token: User haven't id or id equals to 0!");

        String token = tokenService.createSaveAndGetNewToken(id);
        respone.addHeader(TokenService.TOKEN_NAME, token);
    }
}

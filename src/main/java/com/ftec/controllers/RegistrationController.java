package com.ftec.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.resources.MailResources;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.UserService;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final TokenService tokenService;
    private final RegistrationService registrationService;
   
    @Autowired
    MailResources mailRes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRegistration {
        private String username;

        private String password;

        private String email;

        private boolean subscribeForNews;
    }

    @Autowired
    public RegistrationController(UserService userService, TokenService tokenService, RegistrationService registrationService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.registrationService = registrationService;
    }

    @RequestMapping(path = "/registr_test", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRegistration userRegistration, HttpServletResponse response) throws UserExistException {
    	String userid = mailRes.getUserid();
    	try {
            User userToSave = registrationService.registerUser(userRegistration);
            userService.registerNewUserAccount(userToSave);
            sendToken(userToSave, response);

            return new ResponseEntity<String>(HttpStatus.CREATED);
        } catch (TokenException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public void sendToken(User user, HttpServletResponse response) throws TokenException {

        long id = user.getId();
        if (id == 0)
            throw new TokenException("Exception while generating user token: User haven't id or id equals to 0!");

        String token = tokenService.createSaveAndGetNewToken(id);
        response.addHeader(TokenService.TOKEN_NAME, token);
    }
}

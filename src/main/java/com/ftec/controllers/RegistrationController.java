package com.ftec.controllers;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user) throws UserExistException {
        try {
            //implement return token
            userService.registerNewUserAccount(user);
            return new ResponseEntity<User>(HttpStatus.CREATED);
        } catch (UserExistException e) {
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }
}

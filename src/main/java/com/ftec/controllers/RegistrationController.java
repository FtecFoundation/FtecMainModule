package com.ftec.controllers;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User createUser(@RequestBody User user) throws UserExistException {
        userService.registerNewUserAccount(user);
        return user;
    }
}

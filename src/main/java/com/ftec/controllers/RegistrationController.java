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
import com.ftec.exceptions.UserExistException;
import com.ftec.services.TokenService;
import com.ftec.services.Implementations.IdManagerImpl;
import com.ftec.services.interfaces.UserService;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final IdManagerImpl idManager;
    private final TokenService tokenService;
    
    @Autowired
    public RegistrationController(UserService userService, IdManagerImpl idManager, TokenService tokenService) {
        this.userService = userService;
        this.idManager = idManager;
        this.tokenService = tokenService;
    }

    @RequestMapping(path = "/registr_test", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user, HttpServletResponse respone) throws UserExistException {
    	try {
            userService.registerNewUserAccount(user);
            sendToken(respone); 
            
            return new ResponseEntity<User>(HttpStatus.CREATED);
        } catch (UserExistException e) {
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }
    
    //TODO fix argument, its always returns token like "1_bla-bla-bla", increments does not works
	public void sendToken(HttpServletResponse respone) {
		respone.addHeader(TokenService.TOKEN_NAME, tokenService.createAndGetToken(idManager.getLastId("ids")));
	}
}

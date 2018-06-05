package com.ftec.controllers;

import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.ReferralLevelThree;
import com.ftec.entities.ReferralLevelTwo;
import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.ReferralService;
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
import java.io.IOException;

@RestController
public class RegistrationController {

    private final UserService userService;
    private final TokenService tokenService;
    private final RegistrationService registrationService;
    private final ReferralService referralService;


    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public MvcResponse createUser(@RequestBody @Valid UserRegistration userRegistration, HttpServletResponse response) throws UserExistException, IOException, IOException {
        try {
            User userToSave = registrationService.registerUser(userRegistration);
            userService.registerNewUserAccount(userToSave);
            long id = userToSave.getId();
            long referrerId = userRegistration.getReferrerId();

            if (id == 0) {
                throw new TokenException("Exception while generating user token: User haven't id or id equals to 0!");
            }

            if (referrerId != 0) {
                referralService.assignReferral(id, referrerId);
            }

            String token = tokenService.createSaveAndGetNewToken(id);
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
        private String username;

        private String password;

        private String email;

        private boolean subscribeForNews;

        private long referrerId;
    }

    @Autowired
    public RegistrationController(UserService userService, TokenService tokenService, RegistrationService registrationService, ReferralService referralService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.registrationService = registrationService;
        this.referralService = referralService;
    }
}

package com.ftec.controllers;

import com.ftec.constratints.UniqueEmail;
import com.ftec.constratints.UniqueLogin;
import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.Implementations.RegistrationServiceImpl;
import com.ftec.services.TokenService;
import com.ftec.services.interfaces.ReferralService;
import com.ftec.services.interfaces.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
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
import java.util.stream.Collectors;

@RestController
public class RegistrationController {

    private final TokenService tokenService;
    private final RegistrationService registrationService;
    private final ReferralService referralService;


    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public MvcResponse createUser(@RequestBody @Valid UserRegistration userRegistration, BindingResult br, HttpServletResponse response) throws UserExistException, IOException, IOException {
        try {

            if (br.hasErrors()) {
                response.setStatus(400);
                return MvcResponse.getMvcErrorResponse(400, br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("")));
            }

            User userToSave = RegistrationServiceImpl.registerUser(userRegistration);
            registrationService.registerNewUserAccount(userToSave);
            long referrerId = userRegistration.getReferrerId();
            if (referrerId != 0) {
                referralService.assignReferral(userToSave.getId(), referrerId);
            }

            String token = tokenService.createSaveAndGetNewToken(userToSave.getId());
            return new MvcResponse(200, "token", token);
        } catch (TokenException e) {
            response.setStatus(403);
            return MvcResponse.getMvcErrorResponse(403, "TokenNotCreated");
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRegistration {

        @NotNull
        @Size(min = 4, max = 20)
        @UniqueLogin
        private String username;

        @NotNull
        @Size(max = 20)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
        private String password;

        @NotNull
        @UniqueEmail
        @Size(max = 20)
        private String email;

        private boolean subscribeForNews;

        private long referrerId;
    }

    @Autowired
    public RegistrationController(TokenService tokenService, RegistrationService registrationService, ReferralService referralService) {
        this.referralService = referralService;
        this.tokenService = tokenService;
        this.registrationService = registrationService;
    }
}
package com.ftec.controllers;

import com.ftec.constratints.*;
import com.ftec.entities.User;
import com.ftec.exceptions.token.TokenException;
import com.ftec.resources.enums.Statuses;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.Implementations.RegistrationServiceImpl;
import com.ftec.services.interfaces.ReferralService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.TokenService;
import com.ftec.utils.Logger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RegistrationController {

    private final TokenService tokenService;
    private final RegistrationService registrationService;
    private final ReferralService referralService;
    private final UniqueLoginValidator uniqueLoginValidator;
    private final UniqueEmailValidator uniqueEmailValidator;

    @PostMapping(path = "/registration", consumes = "application/json", produces = "application/json")
    public MvcResponse createUser(@RequestBody @Valid UserRegistration userRegistration, BindingResult br, HttpServletResponse response) {
        try {

            if (br.hasErrors()) {
                response.setStatus(400);
                List<FieldError> errors = br.getFieldErrors();
                for (FieldError error : errors) {
                    if (error.getField().equals("username")) {
                        return MvcResponse.getMvcErrorResponse(Statuses.LoginTaken.getStatus(), "This login already taken");
                    }
                    if (error.getField().equals("email")) {
                        return MvcResponse.getMvcErrorResponse(Statuses.EmailTaken.getStatus(), "This email already taken");
                    }
                }
                return MvcResponse.getMvcErrorResponse(Statuses.ModelMalformed.getStatus(), br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("")));
            }

            User userToSave = RegistrationServiceImpl.registerUser(userRegistration);
            registrationService.registerNewUserAccount(userToSave);

            long referrerId = userRegistration.getReferrerId();
            if (referrerId != 0) {
                referralService.assignReferral(userToSave.getId(), referrerId);
            }

            String token = tokenService.createSaveAndGetNewToken(userToSave.getId());
            response.setStatus(200);
            return new MvcResponse(Statuses.Ok.getStatus(), "token", token);
        } catch (TokenException e) {
            Logger.logException("Registration Controller while generation token", e, true);
            response.setStatus(403);
            return MvcResponse.getMvcErrorResponse(Statuses.TokenNotCreated.getStatus(), "Token Not Created");
        } catch (Exception e) {
            Logger.logException("Registration Controller while register user", e, true);
            response.setStatus(500);
            return MvcResponse.getMvcErrorResponse(Statuses.UnexpectedError.getStatus(), "Unexpected error");
        }
    }

    @GetMapping(value = "/checkUniqueLogin", consumes = "application/json", produces = "application/json")
    public MvcResponse checkUniqueLogin(@RequestParam("login") String login, HttpServletResponse response) {
        if (!uniqueLoginValidator.isValid(login, null)) {
            response.setStatus(400);
            return new MvcResponse(Statuses.LoginTaken.getStatus(), "Login already taken");
        }
        return new MvcResponse(Statuses.Ok.getStatus(), "available", true);
    }

    @GetMapping(value = "/checkUniqueEmail", consumes = "application/json", produces = "application/json")
    public MvcResponse checkUniqueEmail(@RequestParam("email") String email, HttpServletResponse response) {
        if (!uniqueEmailValidator.isValid(email, null)) {
            response.setStatus(400);
            return new MvcResponse(Statuses.EmailTaken.getStatus(), "Email already taken");
        }
        return new MvcResponse(Statuses.Ok.getStatus(), "available", true);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRegistration {

        @NotNull
        @Size(min = 4, max = 40)
        @UniqueLogin
        private String username;

        @NotNull
        @Size(max = 40)
        @Pattern(regexp = Patterns.PASSWORD_PATTERN)
        private String password;

        @NotNull
        @UniqueEmail
        @Size(max = 40)
        private String email;

        private boolean subscribeForEmail;

        private long referrerId;
    }

    @Autowired
    public RegistrationController(TokenService tokenService, RegistrationService registrationService, ReferralService referralService, UniqueLoginValidator uniqueLoginValidator, UniqueEmailValidator uniqueEmailValidator) {
        this.referralService = referralService;
        this.tokenService = tokenService;
        this.registrationService = registrationService;
        this.uniqueLoginValidator = uniqueLoginValidator;
        this.uniqueEmailValidator = uniqueEmailValidator;
    }
}
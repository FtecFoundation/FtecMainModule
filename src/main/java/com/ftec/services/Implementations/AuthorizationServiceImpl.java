package com.ftec.services.Implementations;

import com.ftec.controllers.AuthorizationController;
import com.ftec.entities.User;
import com.ftec.exceptions.AuthorizationException;
import com.ftec.exceptions.TwoStepVerificationException;
import com.ftec.services.interfaces.AuthorizationService;
import com.ftec.utils.encoding.PasswordUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {


    private final Environment environment;

    public AuthorizationServiceImpl(Environment environment) {
        this.environment = environment;
    }

    public void authorizate(Optional<User> userOpt, AuthorizationController.UserAuth userAuth) throws AuthorizationException, TwoStepVerificationException {
        if(userOpt.isPresent() && PasswordUtils.isPasswordMatch(userAuth.getPassword(), userOpt.get().getPassword(),userOpt.get().getSalt())) {
            check2FaCode(userAuth.getCode(), userOpt.get());
        }
        else throw new AuthorizationException(INVALID_USERNAME_OR_PASSWORD);
    }

    private void check2FaCode(String twoStepVerCode, User user) throws TwoStepVerificationException {
        if(!user.getTwoStepVerification()) return;

        if(twoStepVerCode == null || twoStepVerCode.length() == 0 ) throw new TwoStepVerificationException(WRONG_2FA_CODE);

        for(String profile: this.environment.getActiveProfiles()){
            if(profile.equals("test")) return;
        }

        throw new TwoStepVerificationException(WRONG_2FA_CODE);

    }
}

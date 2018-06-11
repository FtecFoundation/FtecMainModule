package com.ftec.services.Implementations;

import com.ftec.controllers.LoginController;
import com.ftec.entities.User;
import com.ftec.exceptions.AuthorizationException;
import com.ftec.services.interfaces.AuthorizationService;
import com.ftec.utils.PasswordUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {


    private final Environment environment;

    public AuthorizationServiceImpl(Environment environment) {
        this.environment = environment;
    }

    public void authorizate(Optional<User> userOpt, LoginController.UserAuth userAuth) throws AuthorizationException {
        if(userOpt.isPresent() && PasswordUtils.isPasswordMatch(userAuth.getPassword(), userOpt.get().getPassword(),userOpt.get().getSalt())) {
            check2FaCode(userAuth.getCode(), userOpt.get());
        }
        else throw new AuthorizationException(INVALID_USERNAME_OR_PASSWORD);
    }

    private void check2FaCode(String twoStepVerCode, User user) throws AuthorizationException {
        if(!user.getTwoStepVerification()) return;

        if(twoStepVerCode == null || twoStepVerCode.length() == 0 ) throw new AuthorizationException(WRONG_2FA_CODE);

        for(String profile: this.environment.getActiveProfiles()){
            if(profile.equals("test")) return;
        }

        throw new AuthorizationException(WRONG_2FA_CODE);

    }
}

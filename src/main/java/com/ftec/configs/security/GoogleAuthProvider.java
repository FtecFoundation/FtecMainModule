package com.ftec.configs.security;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class GoogleAuthProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String verificationCode = ((AuthenticationDetails) auth.getDetails()).getVerificationCode();
        GoogleAuthenticator gauth = new GoogleAuthenticator();
        if (!isValidLong(verificationCode) || !gauth.authorize(userDetails.getSecret(), Integer.parseInt(verificationCode))) {
            throw new BadCredentialsException("External system authentication failed");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(PreAuthenticatedAuthenticationToken.class);
    }

    private boolean isValidLong(String verificationCode) {
        try {
            Integer.parseInt(verificationCode);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

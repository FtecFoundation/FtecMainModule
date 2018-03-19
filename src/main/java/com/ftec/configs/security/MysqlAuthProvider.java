package com.ftec.configs.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class MysqlAuthProvider extends DaoAuthenticationProvider {
    private final PasswordEncoder passwordEncoder;

    MysqlAuthProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder=passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        CustomUserDetails userDetails = (CustomUserDetails) getUserDetailsService().loadUserByUsername(auth.getName());
        if(!userDetails.isAccountNonLocked()){
            throw new BadCredentialsException("Account locked");
        }
        if(!passwordEncoder.matches(auth.getCredentials().toString(),userDetails.getPassword())){
            throw new BadCredentialsException("Password wrong");
        }
        if(userDetails.isQrEnabled()){
            return new PreAuthenticatedAuthenticationToken(userDetails, null);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

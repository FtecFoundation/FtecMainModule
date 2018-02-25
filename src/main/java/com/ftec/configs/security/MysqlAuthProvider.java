package com.ftec.configs.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MysqlAuthProvider extends DaoAuthenticationProvider{
    private final PasswordEncoder passwordEncoder;

    public MysqlAuthProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder=passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        CustomUserDetails userDetails = (CustomUserDetails) getUserDetailsService().loadUserByUsername(auth.getName());
        if(!userDetails.isAccountNonLocked()){
            throw new LockedException("Account was banned");
        }
        if(!passwordEncoder.matches(auth.getCredentials().toString(),userDetails.getPassword())){
            throw new BadCredentialsException("Bad credentials");
        }
        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(getUserDetailsService().loadUserByUsername(auth.getName()), result.getCredentials(), result.getAuthorities());
    }
}

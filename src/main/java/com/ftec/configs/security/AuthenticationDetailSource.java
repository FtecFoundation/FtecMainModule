package com.ftec.configs.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class AuthenticationDetailSource extends WebAuthenticationDetailsSource{
    @Override
    public AuthenticationDetails buildDetails(HttpServletRequest httpServletRequest) {
        return new AuthenticationDetails(httpServletRequest);
    }
}

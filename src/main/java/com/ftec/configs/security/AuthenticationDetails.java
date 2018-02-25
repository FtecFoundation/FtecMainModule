package com.ftec.configs.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationDetails extends WebAuthenticationDetails {
    private String verificationCode;

    public AuthenticationDetails(HttpServletRequest request) {
        super(request);
        verificationCode = request.getParameter("code");
    }
    public String getVerificationCode() {
        return verificationCode;
    }
}

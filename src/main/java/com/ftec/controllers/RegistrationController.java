package com.ftec.controllers;

import com.ftec.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private static final String sessionReferrerAttributeName="referrer";

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("")
    public String renderView(@RequestParam(value = "referrer", defaultValue = "0") long referrer, HttpServletRequest req){
        if(referrer!=0) req.setAttribute(sessionReferrerAttributeName, referrer);
        return "registration";
    }
    @PostMapping("")
    public String registerUser(@Valid RegistrationUser user, BindingResult bindingResult, Model m, HttpServletRequest req){
        if(bindingResult.hasErrors()){
            m.addAttribute("errors",bindingResult.getAllErrors());
            return "registration";
        }
        if(req.getAttribute(sessionReferrerAttributeName)!=null){
            user.setReferrer((long)req.getAttribute(sessionReferrerAttributeName));
        }
        registrationService.registerUser(user);
        return "registered";
    }

    //While registering user enters values not for all @Entity user fields. So we need another POJO to handle registration fields
    public static class RegistrationUser{
        private String username;
        private String password;
        private String email;
        private long referrer;

        public RegistrationUser() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public long getReferrer() {
            return referrer;
        }

        public void setReferrer(long referrer) {
            this.referrer = referrer;
        }
    }
}

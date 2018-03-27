package com.ftec.services;

import com.ftec.configs.security.CustomUserDetails;
import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;
import com.ftec.repositories.interfaces.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
public class RegistrationService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public long registerUser(RegistrationController.RegistrationUser user){
        User qualifiedUser = new User(user);
        qualifiedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.persist(qualifiedUser);
        authUser(qualifiedUser);
        //TODO add partners support
        return qualifiedUser.getId();
    }

    public void authUser(User u){
        CustomUserDetails user = new CustomUserDetails(u);
        Authentication token = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Transactional
    public User getUser(long userId){
        return userDAO.getById(userId);
    }
    @Transactional
    public boolean deleteUser(long userId){
        return userDAO.deleteUser(userId);
    }
}

package com.ftec.configs.security;

import com.ftec.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private String password, login, roles, secret;
    private boolean expired, locked, enabled;

    public CustomUserDetails(User u) {
        this.password = u.getPassword();
        this.login = u.getLogin();
        this.roles = u.getRoles();
        this.secret = u.getGoogleSecret();
        this.locked = u.isBanned();

        //TODO implement this features
        this.expired = false;
        this.enabled = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (String s : roles.split(" ")) {
            authorityList.add(new SimpleGrantedAuthority(s));
        }
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    public String getSecret(){
        return secret;
    }
}
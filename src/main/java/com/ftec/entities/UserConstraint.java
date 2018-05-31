package com.ftec.entities;

public class UserConstraint {

    private String username;

    private String password;

    private String email;

    private boolean subscribeForNews;

    public UserConstraint() {
    }

    public UserConstraint(String username, String password, String email, boolean subscribeForNews) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.subscribeForNews = subscribeForNews;
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

    public boolean isSubscribeForNews() {
        return subscribeForNews;
    }

    public void setSubscribeForNews(boolean subscribeForNews) {
        this.subscribeForNews = subscribeForNews;
    }
}

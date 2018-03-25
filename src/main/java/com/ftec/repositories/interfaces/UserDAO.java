package com.ftec.repositories.interfaces;

import com.ftec.entities.User;

public interface UserDAO {
    void persist(User u);
    void update(User u);
    void delete(User u);
    User getById(long id);
    User getByLogin(String login);
    User getAuthenticatedUser();
    void updateUserLanguage(String newLang, String user);
}

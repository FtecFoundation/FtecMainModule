package com.ftec.repositories;

import com.ftec.entities.User;

public interface UserDAO {
    void persist(User u);
    void update(User u);
    void delete(User u);
    User getById(long id);
    User getByLogin(String login);
    User getAuthenticatedUser();
    void updateUserLanguage(String newLang, String user);
    boolean deleteUser(long userId);

    User getUserByChatId(Long chatId);

    double getUserBalance(long userId);

    String getLocale(long userId);
}

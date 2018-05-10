package com.ftec.repositories.interfaces;


import com.ftec.entities.TelegramSettings;

public interface TelegramSettingsDAO {
    void persist(TelegramSettings settings);
    void update(TelegramSettings settings);
    void updateNotifications(long userId, String notifications);
    TelegramSettings get(long id);
    void deleteCode(long userId);
    void saveCode(long userId, String newCode);
}

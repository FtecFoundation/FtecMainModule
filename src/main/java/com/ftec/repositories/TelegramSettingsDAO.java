package com.ftec.repositories;

import com.ftec.entities.TelegramSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramSettingsDAO extends JpaRepository<TelegramSettings, String> {
    TelegramSettings getByUserId(long userId);

}

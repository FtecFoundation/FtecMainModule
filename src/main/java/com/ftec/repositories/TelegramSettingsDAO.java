package com.ftec.repositories;

import com.ftec.entities.TelegramSettings;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TelegramSettingsDAO extends ElasticsearchRepository<TelegramSettings, String> {
    TelegramSettings getByUserId(long userId);
    @Query
    void updateCode();
}

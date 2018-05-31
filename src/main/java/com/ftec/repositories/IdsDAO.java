package com.ftec.repositories;

import com.ftec.entities.Ids;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IdsDAO extends ElasticsearchRepository<Ids, String> {
    Ids findByTableName(String tableName);
}

package com.ftec.repositories;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ftec.entities.Ids;

public interface IdsDAO extends ElasticsearchRepository<Ids, String> {
    Ids findByTableName(String tableName);
    @Query(value = "{\"script\": {\"source\": \"ctx._source.lastId++\"}, \"query\": {\"match\": {\"tableName\": \"?0\"}}")
    void incrementLastId(String tableName);
}

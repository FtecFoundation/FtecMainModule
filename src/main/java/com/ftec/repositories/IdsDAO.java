package com.ftec.repositories;

import com.ftec.entities.Ids;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IdsDAO extends ElasticsearchRepository<Ids, String> {
    Ids findByTableName(String tableName);

    //TODO search for more convenient solution
    default void incrementLastId(String tableName){
        Ids id = findByTableName(tableName);
        id.incrementIndex();
        save(id);
    };
}

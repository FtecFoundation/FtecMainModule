package com.ftec.services.interfaces;

import com.ftec.entities.Ids;

public interface IdManager {
    long getLastId(Class table);
    long getLastId(String tableName);
    void incrementLastId(Class table);
    void incrementLastId(String tableName);
	Iterable<Ids> findAll();
	Ids findByTableName(Class table);
	Ids findByTableName(String table);

}

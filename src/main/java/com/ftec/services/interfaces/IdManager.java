package com.ftec.services.interfaces;

public interface IdManager {
    long getLastId(Class table);
    long getLastId(String tableName);
    void incrementLastId(Class table);
    void incrementLastId(String tableName);
}

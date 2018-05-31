package com.ftec.services.Implementations;

import org.springframework.stereotype.Service;

import com.ftec.entities.Ids;
import com.ftec.repositories.IdsDAO;
import com.ftec.services.interfaces.IdManager;

@Service
public class IdManagerImpl implements IdManager {
    private final IdsDAO idsDAO;

    public IdManagerImpl(IdsDAO idsDAO) {
        this.idsDAO = idsDAO;
    }

    @Override
    public long getLastId(Class table) {
        return getLastId(table.getName());
    }

    @Override
    public long getLastId(String tableName) {
        Ids ids = findByTableName(tableName);
        return ids.getLastId();
    }

    @Override
    public void incrementLastId(Class table) {
        incrementLastId(table.getName());
    }

    @Override
    public void incrementLastId(String tableName) {
        Ids id = findByTableName(tableName);
        id.incrementIndex();
        idsDAO.save(id);
    }
    private Ids findByTableName(String tableName){
        Ids ids = idsDAO.findByTableName(tableName);
        if(ids == null){
            ids = new Ids(tableName, 1);
            idsDAO.save(ids);
        }
        return ids;
    }
}

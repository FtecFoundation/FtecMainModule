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
        Ids ids = idsDAO.findByTableName(tableName);
        if(ids == null){
            ids = new Ids(tableName, 1);
            idsDAO.save(ids);
        }
        return ids.getLastId();
    }

    @Override
    public void incrementLastId(Class table) {
        incrementLastId(table.getName());
    }

    @Override
    public void incrementLastId(String tableName) {
        idsDAO.incrementLastId(tableName);
    }

	@Override
	public Iterable<Ids> findAll() {
		
		return idsDAO.findAll();
	}

	@Override
	public Ids findByTableName(Class table) {
		return idsDAO.findByTableName(table.getName());
	}

	@Override
	public Ids findByTableName(String table) {
		return idsDAO.findByTableName(table);

	}
}

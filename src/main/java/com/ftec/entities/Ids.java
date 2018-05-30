package com.ftec.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "#{@idsIndex}")
public class Ids {
	
    @Id
    private String tableName = "";
    private long lastId = 0;

    public Ids() {
    }

    public Ids(String tableName, long lastId) {
        this.tableName = tableName;
        this.lastId = lastId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getLastId() {
        return lastId;
    }

    public void setLastId(long lastId) {
        this.lastId = lastId;
    }

    public void incrementIndex(){
        lastId++;
    }

	@Override
	public String toString() {
		return "Ids [tableName=" + tableName + ", lastId=" + lastId + "]";
	}
}

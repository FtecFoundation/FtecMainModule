package com.ftec.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class IdManagement {

    @Column
    private static long lastId = 0;

    public IdManagement() {
    }

    public static long getLastId(Class<User> userClass) {
        return lastId;
    }

    public static void setLastId(long lastId) {
        IdManagement.lastId = lastId;
    }

    public static long incrementLastId() {
        return lastId++;
    }
}
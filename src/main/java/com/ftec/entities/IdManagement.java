package com.ftec.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class IdManagement {

    @Column
    private long lastUserId = 0;

    @Column
    private long lastPaymentId = 0;

    public IdManagement() {
        lastUserId++;
        lastPaymentId++;
    }

    public long getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(long lastUserId) {
        this.lastUserId = lastUserId;
    }

    public long getLastPaymentId() {
        return lastPaymentId;
    }

    public void setLastPaymentId(long lastPaymentId) {
        this.lastPaymentId = lastPaymentId;
    }


}

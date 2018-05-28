package com.ftec.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class IdManagement {

    @Column
    private static long lastUserId = 0;

    @Column
    private static long lastPaymentId = 0;

    public IdManagement() {
    }

    public static long getLastUserId() {
        return lastUserId;
    }

    public static void setLastUserId(long lastUserId) {
        IdManagement.lastUserId = lastUserId;
    }

    public static long getLastPaymentId() {
        return lastPaymentId;
    }

    public static void setLastPaymentId(long lastPaymentId) {
        IdManagement.lastPaymentId = lastPaymentId;
    }
}
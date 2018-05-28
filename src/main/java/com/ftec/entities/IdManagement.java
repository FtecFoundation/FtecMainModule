package com.ftec.entities;

public class IdManagement {

    private static long lastUserId = 0;

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

    public static long incrementLastUserId() {
        return lastUserId++;
    }

    public static long incrementLastPaymentId() {
        return lastPaymentId++;
    }
}

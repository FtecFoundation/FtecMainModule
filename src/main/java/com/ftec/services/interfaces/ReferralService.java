package com.ftec.services.interfaces;

public interface ReferralService {

    long getReferrerForUser(long userId);

    double getTotalBalance(long id);

    void assignReferral(long userId, long referrerId);
}

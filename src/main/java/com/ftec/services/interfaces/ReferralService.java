package com.ftec.services.interfaces;

public interface ReferralService {

    void assignReferral(long userId, long referrerId);

    long getReferrerForUser(long userId);

    double getTotalBalanceForAllLevels();

    double getTotalBalanceForLevelOne();

    double getTotalBalanceForLevelTwo();

    double getTotalBalanceForLevelThree();

}

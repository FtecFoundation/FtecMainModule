package com.ftec.repositories;

import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.ReferralLevelThree;
import com.ftec.entities.ReferralLevelTwo;

public interface ReferralDAO {

    void saveReferralLevelOne(ReferralLevelOne referralLevelOne);

    void saveReferralLevelTwo(ReferralLevelTwo referralLevelTwo);

    void saveReferralLevelThree(ReferralLevelThree referralLevelThree);

    long findReferrerForUser(long userId);

    double findTotalBalanceForAllLevels();

    double findTotalBalanceForLevelOne();

    double findTotalBalanceForLevelTwo();

    double findTotalBalanceForLevelThree();

    double findTotalBalanceFromReferralsForUser(long id);
}
package com.ftec.services.interfaces;

import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.ReferralLevelThree;
import com.ftec.entities.ReferralLevelTwo;

public interface ReferralService {

    void saveReferralLevelOne(ReferralLevelOne referralLevelOne);

    void saveReferralLevelTwo(ReferralLevelTwo referralLevelTwo);

    void saveReferralLevelThree(ReferralLevelThree referralLevelThree);

    long getReferrerForUser(long userId);

    double getTotalBalance(long id);

    void assignReferral(long userId, long referrerId);
}

package com.ftec.services.Implementations;

import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.ReferralLevelThree;
import com.ftec.entities.ReferralLevelTwo;
import com.ftec.repositories.ReferralDAO;
import com.ftec.services.interfaces.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReferralServiceImpl implements ReferralService {

    private final ReferralDAO referralDAO;

    @Autowired
    public ReferralServiceImpl(ReferralDAO referralDAO) {
        this.referralDAO = referralDAO;
    }

    @Override
    @Transactional
    public void saveReferralLevelOne(ReferralLevelOne referralLevelOne) {
        referralDAO.saveReferralLevelOne(referralLevelOne);
    }

    @Override
    @Transactional
    public void saveReferralLevelTwo(ReferralLevelTwo referralLevelTwo) {
        referralDAO.saveReferralLevelTwo(referralLevelTwo);
    }

    @Override
    @Transactional
    public void saveReferralLevelThree(ReferralLevelThree referralLevelThree) {
        referralDAO.saveReferralLevelThree(referralLevelThree);
    }

    @Override
    public long getReferrerForUser(long userId) {
        return referralDAO.findReferrerForUser(userId);
    }

    @Override
    public double getTotalBalance(long id) {
        return referralDAO.findTotalBalance(id);
    }

    @Override
    @Transactional
    public void assignReferral(long userId, long referrerId) {
        //1 Save userId in level one
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId, referrerId, 0));

        //2 Check who invited referredId
        long referrerForReferrer = referralDAO.findReferrerForUser(referrerId);
        if (referrerForReferrer != 0) {
            // 2.2 Save userId in level two
            referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(userId, referrerForReferrer, 0));

            // 3 Check who invited invitedForInvitedId
            long referrerForReferrerForReferrer = referralDAO.findReferrerForUser(referrerForReferrer);
            if (referrerForReferrerForReferrer != 0) {
                // 3.2 Save userId in level three
                referralDAO.saveReferralLevelThree(new ReferralLevelThree(userId, referrerForReferrerForReferrer, 0));
            }
        }
    }
}

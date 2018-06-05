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
    public double getTotalBalance(long id) {
        return referralDAO.findTotalBalance(id);
    }
}

package com.ftec.services.Implementations;

import com.ftec.repositories.ReferralDAO;
import com.ftec.services.interfaces.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferralServiceImpl implements ReferralService {

    private final ReferralDAO referralDAO;

    @Autowired
    public ReferralServiceImpl(ReferralDAO referralDAO) {
        this.referralDAO = referralDAO;
    }

    @Override
    public double getTotalBalance(long id) {
        return referralDAO.findTotalBalance(id);
    }
}

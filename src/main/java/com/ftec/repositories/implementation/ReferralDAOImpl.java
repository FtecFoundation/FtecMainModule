package com.ftec.repositories.implementation;

import com.ftec.repositories.ReferralDAO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ReferralDAOImpl implements ReferralDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public double findTotalBalance(long user) {
        String query = "SELECT SUM(balance) FROM (" +
                "SELECT * FROM referral_level_one one " +
                "UNION SELECT * FROM referral_level_two two " +
                "UNION SELECT * FROM referral_level_three three) " +
                "WHERE user = " + user;
        return entityManager.createQuery(query, Double.class).getSingleResult();
    }
}

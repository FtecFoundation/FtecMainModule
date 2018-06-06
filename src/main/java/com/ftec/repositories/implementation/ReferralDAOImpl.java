package com.ftec.repositories.implementation;

import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.ReferralLevelThree;
import com.ftec.entities.ReferralLevelTwo;
import com.ftec.repositories.ReferralDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ReferralDAOImpl implements ReferralDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public void saveReferralLevelOne(ReferralLevelOne referralLevelOne) {
        String query = "INSERT INTO referral_level_one (user_id, referrer_id, balance) " +
                "VALUES (" + referralLevelOne.getUserId() + ", " + referralLevelOne.getReferrerId() + ", " + referralLevelOne.getBalance() + ");";
//        String query = "";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void saveReferralLevelTwo(ReferralLevelTwo referralLevelTwo) {
        String query = "INSERT INTO referral_level_one (user_id, referrer_id, balance) " +
                "VALUES (" + referralLevelTwo.getUserId() + ", " + referralLevelTwo.getReferrerId() + ", " + referralLevelTwo.getBalance() + ");";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void saveReferralLevelThree(ReferralLevelThree referralLevelThree) {
        String query = "INSERT INTO referral_level_one (user_id, referrer_id, balance) " +
                "VALUES (" + referralLevelThree.getUserId() + ", " + referralLevelThree.getReferrerId() + ", " + referralLevelThree.getBalance() + ");";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    public long findReferrerForUser(long userId) {
        //todo find bug in query
        String query = "SELECT referrer_id FROM referral_level_one  WHERE user_id = " + userId + ";";
        return entityManager.createQuery(query, Long.class).getSingleResult();
    }

    @Override
    public double findTotalBalance(long user) {
        String query = "SELECT SUM(balance) FROM (" +
                "SELECT * FROM rreferral_level_one one " +
                "UNION SELECT * FROM referral_level_two two " +
                "UNION SELECT * FROM referral_level_three three " +
                "WHERE user = " + user + ") AS totalBalance;";
        return entityManager.createQuery(query, Double.class).getSingleResult();
    }
}

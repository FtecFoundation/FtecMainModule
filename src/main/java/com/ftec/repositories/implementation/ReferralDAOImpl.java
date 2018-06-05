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
        String query = "INSERT INTO referral_level_one (user_Id, referrer_Id, balance) " +
                "VALUES (" + referralLevelOne.getUserId() + ", " + referralLevelOne.getReferrerId() +
                ", " + referralLevelOne.getBalance() + ";";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void saveReferralLevelTwo(ReferralLevelTwo referralLevelTwo) {
        String query = "INSERT INTO referral_level_two (user_Id, referrer_Id, balance) " +
                "VALUES (" + referralLevelTwo.getUserId() + ", " + referralLevelTwo.getReferrerId() +
                ", " + referralLevelTwo.getBalance() + ";";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void saveReferralLevelThree(ReferralLevelThree referralLevelThree) {
        String query = "INSERT INTO referral_level_three (user_Id, referrer_Id, balance) " +
                "VALUES (" + referralLevelThree.getUserId() + ", " + referralLevelThree.getReferrerId() +
                ", " + referralLevelThree.getBalance() + ";";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    public long findReferrerForUser(long userId) {
        String query = "SELECT referrer_id FROM referral_level_one WHERE user_id = " + userId;
        return entityManager.createQuery(query, Long.class).getSingleResult();
    }

    @Override
    public double findTotalBalance(long user) {
        String query = "SELECT SUM(balance) FROM (" +
                "SELECT * FROM referral_level_one one " +
                "UNION SELECT * FROM referral_level_two two " +
                "UNION SELECT * FROM referral_level_three three " +
                "WHERE user = " + user + ") AS totalBalance;";
        return entityManager.createQuery(query, Double.class).getSingleResult();
    }
}

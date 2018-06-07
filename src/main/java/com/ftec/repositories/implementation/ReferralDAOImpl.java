package com.ftec.repositories.implementation;

import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.ReferralLevelThree;
import com.ftec.entities.ReferralLevelTwo;
import com.ftec.repositories.ReferralDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;

@Repository
public class ReferralDAOImpl implements ReferralDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public void saveReferralLevelOne(ReferralLevelOne referralLevelOne) {
        String query = "INSERT INTO referral_level_one (user_id, referrer_id, balance) " +
                "VALUES (" + referralLevelOne.getUserId() + ", " + referralLevelOne.getReferrerId() + ", " + referralLevelOne.getBalance() + ");";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void saveReferralLevelTwo(ReferralLevelTwo referralLevelTwo) {
        String query = "INSERT INTO referral_level_two (user_id, referrer_id, balance) " +
                "VALUES (" + referralLevelTwo.getUserId() + ", " + referralLevelTwo.getReferrerId() + ", " + referralLevelTwo.getBalance() + ");";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void saveReferralLevelThree(ReferralLevelThree referralLevelThree) {
        String query = "INSERT INTO referral_level_three (user_id, referrer_id, balance) " +
                "VALUES (" + referralLevelThree.getUserId() + ", " + referralLevelThree.getReferrerId() + ", " + referralLevelThree.getBalance() + ");";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    public long findReferrerForUser(long userId) {
        try {
            String query = "SELECT referrer_id FROM referral_level_one WHERE user_id = " + userId;
            return ((BigInteger) entityManager.createNativeQuery(query).getSingleResult()).longValue();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public double findTotalBalanceForAllLevels() {
        try {
            String query = "SELECT SUM(balance) FROM (SELECT * FROM referral_level_one UNION SELECT * FROM referral_level_two UNION SELECT * FROM referral_level_three) AS totalBalance";
            return (double) entityManager.createNativeQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return 0.0;
        }
    }

    @Override
    public double findTotalBalanceForLevelOne() {
        try {
            String query = "SELECT SUM(balance) FROM (SELECT * FROM referral_level_one) AS totalBalance";
            return (double) entityManager.createNativeQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return 0.0;
        }
    }

    @Override
    public double findTotalBalanceForLevelTwo() {
        try {
            String query = "SELECT SUM(balance) FROM (SELECT * FROM referral_level_two) AS totalBalance";
            return (double) entityManager.createNativeQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return 0.0;
        }
    }

    @Override
    public double findTotalBalanceForLevelThree() {
        try {
            String query = "SELECT SUM(balance) FROM (SELECT * FROM referral_level_three) AS totalBalance";
            return (double) entityManager.createNativeQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return 0.0;
        }
    }

    @Override
    public double findTotalBalanceFromReferralsForUser(long id) {
        try {
            String query = "SELECT SUM(balance) FROM (SELECT * FROM referral_level_one one WHERE one.referrer_id = " + id +
                    " UNION SELECT * FROM referral_level_two two WHERE two.referrer_id =  " + id +
                    " UNION SELECT * FROM referral_level_three three WHERE three.referrer_id = " + id + ") as TotalBalance";
            return (double) entityManager.createNativeQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return 0.0;
        }
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM referral_level_one; " +
                "DELETE FROM referral_level_two; " +
                "DELETE FROM referral_level_three";
        entityManager.createNativeQuery(query);
    }
}

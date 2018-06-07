package com.ftec.repositories;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.ReferralLevelThree;
import com.ftec.entities.ReferralLevelTwo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
public class ReferralDAOTest {

    @Autowired
    ReferralDAO referralDAO;

    @Before
    public void setUp() {
        referralDAO.deleteAll();
    }

    @Test
    public void saveReferralLevelOneTest() throws Exception {
        long userId = 2;
        long referrerId = 1;
        double balance = 1.1;
        ReferralLevelOne lvlOne = new ReferralLevelOne(userId, referrerId, balance);
        referralDAO.saveReferralLevelOne(lvlOne);

        assertNotNull(referralDAO.findReferralLevelOneForUser(userId));
    }

    @Test
    public void saveReferralLevelTwoTest() throws Exception {
        long userId = 4;
        long referrerId = 3;
        double balance = 1.22;
        ReferralLevelTwo lvlTwo = new ReferralLevelTwo(userId, referrerId, balance);
        referralDAO.saveReferralLevelTwo(lvlTwo);

        assertNotNull(referralDAO.findReferralLevelTwoForUser(userId));
    }

    @Test
    public void saveReferralLevelThreeTest() throws Exception {
        long userId = 6;
        long referrerId = 5;
        double balance = 1.33;
        ReferralLevelThree lvlThree = new ReferralLevelThree(userId, referrerId, balance);
        referralDAO.saveReferralLevelThree(lvlThree);

        assertNotNull(referralDAO.findReferralLevelThreeForUser(userId));
    }

    @Test
    public void deleteAllReferralsTest() throws Exception {
        long userId = 8;
        long referrerId = 7;
        double balance = 0.0;
        ReferralLevelOne lvlOne = new ReferralLevelOne(userId, referrerId, balance);
        referralDAO.saveReferralLevelOne(lvlOne);
        referralDAO.deleteAll();

        assertNull(referralDAO.findReferralLevelOneForUser(userId));
    }

    @Test
    public void findReferralLevelOneTest() throws Exception {
        long userId = 10;
        long referrerId = 9;
        double balance = 22.22;
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId, referrerId, balance));

        ReferralLevelOne finded = referralDAO.findReferralLevelOneForUser(userId);
        assertNotNull(finded);
    }

    @Test
    public void findReferralLevelTwoTest() throws Exception {
        long userId = 12;
        long referrerId = 11;
        double balance = 33.33;
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(userId, referrerId, balance));

        ReferralLevelTwo finded = referralDAO.findReferralLevelTwoForUser(userId);
        assertNotNull(finded);
    }

    @Test
    public void findReferralLevelThreeTest() throws Exception {
        long userId = 14;
        long referrerId = 13;
        double balance = 35.3;
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(userId, referrerId, balance));

        ReferralLevelThree finded = referralDAO.findReferralLevelThreeForUser(userId);
        assertNotNull(finded);
    }

    @Test
    public void findReferrerForUserTest() throws Exception {
        long userId1 = 16;
        long referrerId1 = 15;
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId1, referrerId1, 21.21));

        long userId2 = 17;
        long referrerId2 = userId1;
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId2, referrerId2, 22.22));

        long referrerForReferrerFromDb = referralDAO.findReferrerForUser(referrerId2);
        assertEquals(referrerForReferrerFromDb, referrerId1);
    }

    @Test
    public void findTotalBalanceForLevelOneTest() throws Exception {
        double balance1 = 215.221515;
        double balance2 = 111.222222;
        double totalBalance = balance1 + balance2;

        referralDAO.saveReferralLevelOne(new ReferralLevelOne(20, 19, balance1));
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(22, 21, balance2));

        double totalBalanceLvlOneFromDB = referralDAO.findTotalBalanceForLevelOne();
        assertEquals(totalBalance, totalBalanceLvlOneFromDB, 0.001f);
    }

    @Test
    public void findTotalBalanceForLevelTwoTest() throws Exception {
        double balance1 = 215.221515;
        double balance2 = 111.222222;
        double totalBalance = balance1 + balance2;

        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(24, 23, balance1));
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(26, 25, balance2));

        double totalBalanceLvlTwoFromDB = referralDAO.findTotalBalanceForLevelTwo();
        assertEquals(totalBalance, totalBalanceLvlTwoFromDB, 0.001f);
    }

    @Test
    public void findTotalBalanceForLevelThreeTest() throws Exception {
        double balance1 = 215.221515;
        double balance2 = 111.222222;
        double totalBalance = balance1 + balance2;

        referralDAO.saveReferralLevelThree(new ReferralLevelThree(28, 27, balance1));
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(30, 29, balance2));

        double totalBalanceLvlThreeFromDB = referralDAO.findTotalBalanceForLevelThree();
        assertEquals(totalBalance, totalBalanceLvlThreeFromDB, 0.001f);
    }

    @Test
    public void findTotalBalanceForAllLevelsTest() throws Exception {
        double balanceForLvlOne = 215.221515;
        double balanceForLvlTwo = 111.222222;
        double balanceForLvlThree = 313.313222;
        double totalBalance = balanceForLvlOne + balanceForLvlTwo + balanceForLvlThree;

        referralDAO.saveReferralLevelOne(new ReferralLevelOne(32, 31, balanceForLvlOne));
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(34, 33, balanceForLvlTwo));
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(36, 35, balanceForLvlThree));

        double balanceForAllLvlsFromDB = referralDAO.findTotalBalanceForAllLevels();
        assertEquals(totalBalance, balanceForAllLvlsFromDB, 0.001f);
    }

    @Test
    public void findTotalBalanceFromReferralsForUserTest() throws Exception {

        long referrerId = 38;

        double balForLvlOne = 1.1;
        double balForLvlTwo = 2.2;
        double balForLvlThree = 3.3;

        referralDAO.saveReferralLevelOne(new ReferralLevelOne(39, referrerId, balForLvlOne));
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(40, referrerId, balForLvlTwo));
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(41, referrerId, balForLvlThree));

        double totalBalance = balForLvlOne + balForLvlTwo + balForLvlThree;
        double totalBalForRefFromUserFromDB = referralDAO.findTotalBalanceFromReferralsForUser(38);
        assertEquals(totalBalance, totalBalForRefFromUserFromDB, 0.001f);
    }
}

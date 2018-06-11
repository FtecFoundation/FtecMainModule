package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.ReferralLevelThree;
import com.ftec.entities.ReferralLevelTwo;
import com.ftec.repositories.ReferralDAO;
import com.ftec.services.interfaces.ReferralService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class ReferralServiceTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserService userService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    ReferralService referralService;

    @Autowired
    ReferralDAO referralDAO;

    @Before
    public void setUp() {
        referralDAO.deleteAll();
    }

    @Test
    public void assignReferralLevelOneTest() throws Exception {
        long userId = 2;
        long referrerId = 1;
        referralService.assignReferral(userId, referrerId); //Should be in level one 2, 1, 0
        assertNotNull(referralDAO.findReferralLevelOneForUser(userId));
    }

    @Test
    public void assignReferralLevelTwoTest() throws Exception {
        long userId = 2;
        long referrerId = 1;
        referralService.assignReferral(userId, referrerId); //Should be in level one 2, 1, 0

        long userId2 = 3;
        referralService.assignReferral(userId2, userId); //Should be in level two 3, 1, 0
        assertNotNull(referralDAO.findReferralLevelTwoForUser(userId2));
    }

    @Test
    public void assignReferralLevelThreeTest() throws Exception {
        long userId = 2;
        long referrerId = 1;
        referralService.assignReferral(userId, referrerId); //Should be in level one 2, 1, 0

        long userId2 = 3;
        referralService.assignReferral(userId2, userId); //Should be in level two 3, 1, 0

        long userId3 = 4;
        referralService.assignReferral(userId3, userId2); //Should be in level three 4, 1, 0
        assertNotNull(referralDAO.findReferralLevelThreeForUser(userId3));

    }

    @Test
    public void getReferrerForUserTest() throws Exception {
        long userId1 = 2;
        long referrerId1 = 1;
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId1, referrerId1, 21.21));

        long userId2 = 3;
        long referrerId2 = userId1;
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId2, referrerId2, 22.22));

        long referrerForReferrerFromDb = referralService.getReferrerForUser(referrerId2);
        assertEquals(referrerForReferrerFromDb, referrerId1);

    }

    @Test
    public void getTotalBalanceForAllLevelsTest() throws Exception {

        double balanceForLvlOne = 215.221515;
        double balanceForLvlTwo = 111.222222;
        double balanceForLvlThree = 313.313222;
        double totalBalance = balanceForLvlOne + balanceForLvlTwo + balanceForLvlThree;

        referralDAO.saveReferralLevelOne(new ReferralLevelOne(6, 5, balanceForLvlOne));
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(8, 7, balanceForLvlTwo));
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(9, 10, balanceForLvlThree));

        double balanceForAllLvlsFromDB = referralService.getTotalBalanceForAllLevels();
        assertEquals(totalBalance, balanceForAllLvlsFromDB, 0.001f);

    }

    @Test
    public void getTotalBalanceForLevelOneTest() throws Exception {
        double balance1 = 215.221515;
        double balance2 = 111.222222;
        double totalBalance = balance1 + balance2;

        referralDAO.saveReferralLevelOne(new ReferralLevelOne(20, 19, balance1));
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(22, 21, balance2));

        double totalBalanceLvlOneFromDB = referralService.getTotalBalanceForLevelOne();
        assertEquals(totalBalance, totalBalanceLvlOneFromDB, 0.001f);

    }

    @Test
    public void getTotalBalanceForLevelTwoTest() throws Exception {

        double balance1 = 215.221515;
        double balance2 = 111.222222;
        double totalBalance = balance1 + balance2;

        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(24, 23, balance1));
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(26, 25, balance2));

        double totalBalanceLvlTwoFromDB = referralService.getTotalBalanceForLevelTwo();
        assertEquals(totalBalance, totalBalanceLvlTwoFromDB, 0.001f);

    }

    @Test
    public void getTotalBalanceForLevelThreeTest() throws Exception {

        double balance1 = 215.221515;
        double balance2 = 111.222222;
        double totalBalance = balance1 + balance2;

        referralDAO.saveReferralLevelThree(new ReferralLevelThree(28, 27, balance1));
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(30, 29, balance2));

        double totalBalanceLvlThreeFromDB = referralService.getTotalBalanceForLevelThree();
        assertEquals(totalBalance, totalBalanceLvlThreeFromDB, 0.001f);
    }

    @Test
    public void getTotalBalanceFromReferralsForUser() throws Exception {

        long referrerId = 23;

        double balance1 = 1.1;
        double balance2 = 2.2;
        double balance3 = 3.3;

        referralDAO.saveReferralLevelOne(new ReferralLevelOne(24, referrerId, balance1));
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(25, referrerId, balance2));
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(26, referrerId, balance3));

        double totalBalance = balance1 + balance2 + balance3;
        double totalBalanceFromRefsForUserFromDB = referralService.getTotalBalanceFromReferralsForUser(referrerId);
        assertEquals(totalBalance, totalBalanceFromRefsForUserFromDB, 0.001f);

    }
}


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

    /*
    @Test
    public void saveReferralSystemTest() throws Exception {

        //Saving Users without referrer Id (Main users)
        RegistrationController.UserRegistration userRegWithoutReferrer1 = new RegistrationController.UserRegistration("testUser1", "Strong_Pass1", "email1@gmail.com", false, 0);
        RegistrationController.UserRegistration userRegWithoutReferrer2 = new RegistrationController.UserRegistration("testUser2", "Strong_Pass2", "email2@gmail.com", false, 0);
        User userWithoutReferrer1 = RegistrationServiceImpl.registerUser(userRegWithoutReferrer1);
        User userWithoutReferrer2 = RegistrationServiceImpl.registerUser(userRegWithoutReferrer2);
        registrationService.registerNewUserAccount(userWithoutReferrer1);
        registrationService.registerNewUserAccount(userWithoutReferrer2);

        //Level One Testing
        //Saving Users with referrer Id (Referral level one testing)
        RegistrationController.UserRegistration userRegWithReferrer1 = new RegistrationController.UserRegistration("testUser3", "Strong_Pass3", "email3@gmail.com", false, userWithoutReferrer1.getId());//1
        RegistrationController.UserRegistration userRegWithReferrer2 = new RegistrationController.UserRegistration("testUser4", "Strong_Pass4", "email4@gmail.com", false, userWithoutReferrer2.getId());//2
        User userWithReferrer1 = RegistrationServiceImpl.registerUser(userRegWithReferrer1);
        User userWithReferrer2 = RegistrationServiceImpl.registerUser(userRegWithReferrer2);
        registrationService.registerNewUserAccount(userWithReferrer1);
        registrationService.registerNewUserAccount(userWithReferrer2);
        //Referral level One saving
        referralService.saveReferralLevelOne(new ReferralLevelOne(userWithReferrer1.getId(), userRegWithReferrer1.getReferrerId(), 1.1));
        referralService.saveReferralLevelOne(new ReferralLevelOne(userWithReferrer2.getId(), userRegWithReferrer2.getReferrerId(), 1.2));
        //Should be in level One: 3, 1, 1.1;
        //Should be in level One: 4, 2, 1.2;


        //Level Two Testing
        //Saving Users with referrer Id, which have referrer (Referral level two testing)
        RegistrationController.UserRegistration userRegWithReferrer3 = new RegistrationController.UserRegistration("testUser5", "Strong_Pass5", "email5@gmail.com", false, userWithReferrer1.getId());//3
        RegistrationController.UserRegistration userRegWithReferrer4 = new RegistrationController.UserRegistration("testUser6", "Strong_Pass6", "email6@gmail.com", false, userWithReferrer2.getId());//4
        User userWithReferrer3 = RegistrationServiceImpl.registerUser(userRegWithReferrer3);
        User userWithReferrer4 = RegistrationServiceImpl.registerUser(userRegWithReferrer4);
        registrationService.registerNewUserAccount(userWithReferrer3);
        registrationService.registerNewUserAccount(userWithReferrer4);
        //Referral level One saving
        referralService.saveReferralLevelOne(new ReferralLevelOne(userWithReferrer3.getId(), userRegWithReferrer3.getReferrerId(), 1.3));
        referralService.saveReferralLevelOne(new ReferralLevelOne(userWithReferrer4.getId(), userRegWithReferrer4.getReferrerId(), 1.4));
        //Referral level Two saving
        long referrerForReferrer1 = referralService.getReferrerForUser(userRegWithReferrer3.getReferrerId());
        long referrerForReferrer2 = referralService.getReferrerForUser(userRegWithReferrer4.getReferrerId());
        referralService.saveReferralLevelTwo(new ReferralLevelTwo(userWithReferrer3.getId(), referrerForReferrer1, 2.1));
        referralService.saveReferralLevelTwo(new ReferralLevelTwo(userWithReferrer4.getId(), referrerForReferrer2, 2.2));
        //Should be in level Two: 5, 1, 2.1;
        //Should be in level Two: 6, 2, 2.1;

        //Level Three Testing
        //Saving Users with referrer Id, which have referrer, which have referrer (Referral level three testing)
        RegistrationController.UserRegistration userRegWithReferrer5 = new RegistrationController.UserRegistration("testUser7", "Strong_Pass7", "email7@gmail.com", false, userWithReferrer3.getId());//5
        RegistrationController.UserRegistration userRegWithReferrer6 = new RegistrationController.UserRegistration("testUser8", "Strong_Pass8", "email8@gmail.com", false, userWithReferrer4.getId());//6
        User userWithReferrer5 = RegistrationServiceImpl.registerUser(userRegWithReferrer5);
        User userWithReferrer6 = RegistrationServiceImpl.registerUser(userRegWithReferrer6);
        registrationService.registerNewUserAccount(userWithReferrer5);
        registrationService.registerNewUserAccount(userWithReferrer6);
        //Referral level One saving
        referralService.saveReferralLevelOne(new ReferralLevelOne(userWithReferrer5.getId(), userRegWithReferrer5.getReferrerId(), 1.5));
        referralService.saveReferralLevelOne(new ReferralLevelOne(userWithReferrer6.getId(), userRegWithReferrer6.getReferrerId(), 1.6));
        //Referral level Two saving
        long referrerForReferrer3 = referralService.getReferrerForUser(userRegWithReferrer5.getReferrerId());
        long referrerForReferrer4 = referralService.getReferrerForUser(userRegWithReferrer6.getReferrerId());
        referralService.saveReferralLevelTwo(new ReferralLevelTwo(userWithReferrer5.getId(), referrerForReferrer3, 2.3));
        referralService.saveReferralLevelTwo(new ReferralLevelTwo(userWithReferrer6.getId(), referrerForReferrer4, 2.4));
        //Referral level Three saving
        long referrerForReferrerForReferrer1 = referralService.getReferrerForUser(referrerForReferrer3);
        long referrerForReferrerForReferrer2 = referralService.getReferrerForUser(referrerForReferrer4);
        referralService.saveReferralLevelThree(new ReferralLevelThree(userWithReferrer5.getId(), referrerForReferrerForReferrer1, 3.1));
        referralService.saveReferralLevelThree(new ReferralLevelThree(userWithReferrer6.getId(), referrerForReferrerForReferrer2, 3.2));
        //Should be in level Three: 7, 1, 3.1;
        //Should be in level Three: 8, 2, 3.2;

    }*/
}


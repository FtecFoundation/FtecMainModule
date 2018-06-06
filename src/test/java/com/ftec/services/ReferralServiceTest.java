package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.ReferralLevelOne;
import com.ftec.entities.ReferralLevelThree;
import com.ftec.entities.ReferralLevelTwo;
import com.ftec.repositories.ReferralDAO;
import com.ftec.services.interfaces.ReferralService;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.services.interfaces.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
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

    @Test
    public void assignReferralLevelOneTest() throws Exception {
        long userId = 3;
        long referrerId = 1;
        referralService.assignReferral(userId, referrerId); //Should be in level one 3, 1, 0
        assertEquals(referralService.getReferrerForUser(userId), referrerId);
    }

    @Test
    public void getReferrerForUserTest() throws Exception {
        long userId = 4;
        long referrerId = 2;
        referralService.assignReferral(userId, referrerId); //Should be in level one 4, 2, 0

        long referrerForUser = referralService.getReferrerForUser(referrerId); //Should be 0, because 2 has no referrer
        assertEquals(referrerForUser, 0);
    }

    @Test
    public void getTotalBalanceTest() throws Exception {

        long userId = 4;
        long referrerId = 1;
        double balance1 = 1.1;
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId, referrerId, balance1));

        long userId2 = 5;
        long referrerId2 = 2;
        double balance2 = 2.2;
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(userId2, referrerId2, balance2));

        long userId3 = 6;
        long referrerId3 = 3;
        double balance3 = 3.3;
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(userId3, referrerId3, balance3));

        double totalBalance = balance1 + balance2 + balance3;
        assertEquals(totalBalance, referralService.getTotalBalanceForAllLevels(), 0.001f);
    }

    @Test
    public void getTotalBalanceForLevelOneTest() throws Exception {

        long userId = 3;
        long referrerId = 1;
        double balance1 = 111.1111;
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId, referrerId, balance1));

        long userId2 = 4;
        long referrerId2 = 2;
        double balance2 = 112.2222;
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId2, referrerId2, balance2));

        double totalBalance = balance1 + balance2;
        assertEquals(totalBalance, referralService.getTotalBalanceForLevelOne(), 0.001f);
    }

    @Test
    public void getTotalBalanceForLevelTwoTest() throws Exception {

        long userId = 3;
        long referrerId = 1;
        double balance1 = 1.1;
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(userId, referrerId, balance1));

        long userId2 = 4;
        long referrerId2 = 2;
        double balance2 = 2.2;
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(userId2, referrerId2, balance2));

        double totalBalance = balance1 + balance2;
        assertEquals(totalBalance, referralService.getTotalBalanceForLevelTwo(), 0.001f);
    }

    @Test
    public void getTotalBalanceForLevelThreeTest() throws Exception {
        long userId = 3;
        long referrerId = 1;
        double balance1 = 1.1;
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(userId, referrerId, balance1));

        long userId2 = 4;
        long referrerId2 = 2;
        double balance2 = 2.2;
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(userId2, referrerId2, balance2));

        double totalBalance = balance1 + balance2;
        assertEquals(totalBalance, referralService.getTotalBalanceForLevelThree(), 0.001f);
    }

    @Test
    public void getTotalBalanceFromReferralsForUser() throws Exception {
        long userId = 4;
        long referrerId = 1;
        double balance1 = 1.1;
        referralDAO.saveReferralLevelOne(new ReferralLevelOne(userId, referrerId, balance1));

        long userId2 = 5;
        double balance2 = 2.2;
        referralDAO.saveReferralLevelTwo(new ReferralLevelTwo(userId2, referrerId, balance2));

        long userId3 = 6;
        double balance3 = 3.3;
        referralDAO.saveReferralLevelThree(new ReferralLevelThree(userId3, referrerId, balance3));

        double totalBalance = balance1 + balance2 + balance3;
        assertEquals(totalBalance, referralService.getTotalBalanceFromReferralsForUser(referrerId), 0.001f);
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


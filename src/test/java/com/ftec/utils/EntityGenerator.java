package com.ftec.utils;

import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;
import com.ftec.services.Implementations.RegistrationServiceImpl;
import org.springframework.test.context.ActiveProfiles;

import java.util.Random;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
public class EntityGenerator {
    private static long num = 0;

    public static User getNewUser() {
        num++;
        User u = RegistrationServiceImpl.registerUser(getNewRegisrtUser());

        return u;
    }

    public static long getNextNum() {
        num++;
        return num;
    }

    public static RegistrationController.UserRegistration getNewRegisrtUser() {
        num++;
        String password = "Strong_Password_";
        String username = "user_";
        String email1 = "email_";
        String email2 = "@gmail.com";
        long referrerId = 0;
        return new RegistrationController.UserRegistration(username + num,
                password + num, email1 + num + email2, new Random().nextBoolean(), referrerId);
    }
}
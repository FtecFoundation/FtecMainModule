package com.ftec.utils;

import com.ftec.resources.enums.TutorialSteps;
import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;

import java.util.Random;

public class EntityGenerator {
    private static long num = 0;

    private static String username = "user_" ;
    private static String password = "Strong_Password_";
    private static String email1 = "email_" ;
    private static String email2 = "@gmail.com";

    public static User getNewUser(){
        num++;
        User u = new User(username + num,password + num,
                email1 + num + email2,TutorialSteps.FIRST,false,false);
        u.setSalt(PasswordUtils.getSalt(10));
        return u;
    }
    public static long getNextNum(){
        num++;
        return num;
    }
    public static RegistrationController.UserRegistration getNewRegisrtUser(){
        num++;
        return new RegistrationController.UserRegistration(username + num,
                password + num, email1 + num + email2,new Random().nextBoolean());
    }
}
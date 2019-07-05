package com.ftec.utils.constraints;

import java.util.regex.Pattern;

public class Patterns {
    public final static String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$&+,:;=?@#|'<>.^*()%!_\\-]).{8,}$";

    public static boolean validatePass(String pass) {
        return Pattern.compile(PASSWORD_PATTERN).matcher(pass).matches();
    }
}

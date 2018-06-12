package com.ftec.constratints;


import com.ftec.exceptions.WeakPasswordException;

import java.util.regex.Pattern;

public class Patterns {
    public final static String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
    public final static Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static void validatePass(String pass) throws WeakPasswordException {
        if(!pattern.matcher(pass).matches()) throw new WeakPasswordException();
    }

}

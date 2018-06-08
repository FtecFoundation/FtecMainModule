package com.ftec.constratints;


import com.ftec.exceptions.RestoreException;

import java.util.regex.Pattern;

public class Patterns {
    public final static String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
    public final static Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static void validatePass(String pass) throws RestoreException {
        if(!pattern.matcher(pass).matches()) throw new RestoreException("Invalid password!");
    }

}

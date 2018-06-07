package com.ftec.constratints;


import java.io.IOException;
import java.util.regex.Pattern;

public class Patterns {
    public final static String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
    public final static Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    //TODO test it
    public static void validatePass(String pass) throws IOException {
        if(!pattern.matcher(pass).matches()) throw new IOException(("Invalid password!"));
    }
}

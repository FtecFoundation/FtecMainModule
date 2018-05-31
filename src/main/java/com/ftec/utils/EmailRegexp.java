package com.ftec.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailRegexp {
	public static final String regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(regexp);

	public static boolean validate(String emailStr) {
		        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		        return matcher.find();
	}
}

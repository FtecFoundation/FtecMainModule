package com.ftec.exceptions;

import com.ftec.exceptions.token.TokenException;

public class LogoutException extends TokenException {

	public LogoutException(String message) {
		super(message);
	}

}

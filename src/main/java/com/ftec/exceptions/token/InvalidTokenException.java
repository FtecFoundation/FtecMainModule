package com.ftec.exceptions.token;

public class InvalidTokenException extends TokenException{
	private final static String ex = "Invalid token! ";
	public InvalidTokenException(String message) {
		super(ex + message);
		
	}
}

package com.ftec.exceptions;

public class InvalidTokenException extends RuntimeException{
	private final static String ex = "Invalid token! ";
	public InvalidTokenException(String message) {
		super(ex + message);
		
	}
}

package com.ftec.exceptions;

import java.io.IOException;

public class InvalidUpdateDataException extends IOException{
	
	public InvalidUpdateDataException(String message) {
		super("Invalid updated data. " + message);
	}
}

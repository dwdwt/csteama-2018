package com.cs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidParameterException extends RuntimeException{
	
	public InvalidParameterException(String message) {
		super(message);
	}

}

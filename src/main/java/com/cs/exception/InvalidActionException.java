package com.cs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class InvalidActionException extends RuntimeException{
	
	public InvalidActionException(String message) {
		super(message);
	}
}

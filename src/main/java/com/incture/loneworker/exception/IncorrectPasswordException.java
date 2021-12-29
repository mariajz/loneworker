package com.incture.loneworker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class IncorrectPasswordException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String resourceName;
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public IncorrectPasswordException(String resourceName) {
		super(String.format("Incorrect password for %s", resourceName));
		this.resourceName = resourceName;
	}
	
	
	

	
	
}


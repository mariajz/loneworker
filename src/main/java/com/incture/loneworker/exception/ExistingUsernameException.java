package com.incture.loneworker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)

public class ExistingUsernameException extends RuntimeException{
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
	public ExistingUsernameException(String resourceName) {
		super(String.format("Username %s exists, try Login!", resourceName));
		this.resourceName = resourceName;
	}
	
}

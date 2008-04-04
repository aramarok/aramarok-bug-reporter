package com.drey.aramarok.domain.exceptions.login;

public class InvalidPasswordException extends LoginException {
	
	private static final long serialVersionUID = 654321;

	public InvalidPasswordException() {
		super();
	}

	public InvalidPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPasswordException(String message) {
		super(message);
	}

	public InvalidPasswordException(Throwable cause) {
		super(cause);
	}
}
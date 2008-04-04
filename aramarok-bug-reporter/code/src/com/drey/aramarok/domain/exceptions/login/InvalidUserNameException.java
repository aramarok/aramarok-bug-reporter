package com.drey.aramarok.domain.exceptions.login;

public class InvalidUserNameException extends LoginException {
	
	private static final long serialVersionUID = 54321;

	public InvalidUserNameException() {
		super();
	}

	public InvalidUserNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUserNameException(String message) {
		super(message);
	}

	public InvalidUserNameException(Throwable cause) {
		super(cause);
	}
}
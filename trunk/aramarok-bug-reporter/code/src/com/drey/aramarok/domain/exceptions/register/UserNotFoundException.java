package com.drey.aramarok.domain.exceptions.register;

public class UserNotFoundException extends RegisterException {
	private static final long serialVersionUID = 7654321;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}
}
package com.drey.aramarok.domain.exceptions.register;

public class UserNameAlreadyExistsException extends RegisterException{
	private static final long serialVersionUID = 1234321;

	public UserNameAlreadyExistsException() {
		super();
	}

	public UserNameAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNameAlreadyExistsException(String message) {
		super(message);
	}

	public UserNameAlreadyExistsException(Throwable cause) {
		super(cause);
	}
}

package com.drey.aramarok.domain.exceptions.register;

public class NoPasswordException extends RegisterException {
	private static final long serialVersionUID = 7654321;

	public NoPasswordException() {
		super();
	}

	public NoPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoPasswordException(String message) {
		super(message);
	}

	public NoPasswordException(Throwable cause) {
		super(cause);
	}
}
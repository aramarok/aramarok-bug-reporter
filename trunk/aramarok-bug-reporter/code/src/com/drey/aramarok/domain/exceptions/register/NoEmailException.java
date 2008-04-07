package com.drey.aramarok.domain.exceptions.register;

public class NoEmailException extends RegisterException {
	private static final long serialVersionUID = 87654321;

	public NoEmailException() {
		super();
	}

	public NoEmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoEmailException(String message) {
		super(message);
	}

	public NoEmailException(Throwable cause) {
		super(cause);
	}
}
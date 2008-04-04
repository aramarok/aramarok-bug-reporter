package com.drey.aramarok.domain.exceptions.register;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class RegisterException extends NonFatalDomainException {
	private static final long serialVersionUID = 87654321;

	public RegisterException() {
		super();
	}

	public RegisterException(String message, Throwable cause) {
		super(message, cause);
	}

	public RegisterException(String message) {
		super(message);
	}

	public RegisterException(Throwable cause) {
		super(cause);
	}
}
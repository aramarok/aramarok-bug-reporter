package com.drey.aramarok.domain.exceptions.login;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class LoginException extends NonFatalDomainException {
	
	private static final long serialVersionUID = 4321;

	public LoginException() {
		super();
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}
}
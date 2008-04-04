package com.drey.aramarok.domain.exceptions.login;

public class DisabledAccountException extends LoginException {
	
	private static final long serialVersionUID = 7654321;

	public DisabledAccountException() {
		super();
	}

	public DisabledAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public DisabledAccountException(String message) {
		super(message);
	}

	public DisabledAccountException(Throwable cause) {
		super(cause);
	}
}
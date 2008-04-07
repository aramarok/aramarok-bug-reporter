package com.drey.aramarok.domain.exceptions.register;

public class EmailAddressAlreadyRegisteredException extends RegisterException {
	private static final long serialVersionUID = 987654321;

	public EmailAddressAlreadyRegisteredException() {
		super();
	}

	public EmailAddressAlreadyRegisteredException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailAddressAlreadyRegisteredException(String message) {
		super(message);
	}

	public EmailAddressAlreadyRegisteredException(Throwable cause) {
		super(cause);
	}
}
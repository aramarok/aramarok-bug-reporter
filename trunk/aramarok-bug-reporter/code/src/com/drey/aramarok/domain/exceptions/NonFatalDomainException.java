package com.drey.aramarok.domain.exceptions;

public class NonFatalDomainException extends DomainException {

	private static final long serialVersionUID = 321;

	public NonFatalDomainException(String message) {
		super(message);
	}

	public NonFatalDomainException() {
	}

	public NonFatalDomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonFatalDomainException(Throwable cause) {
		super(cause);
	}

}

package com.drey.aramarok.domain.exceptions;

public class ExternalSystemException extends FatalDomainException {

	private static final long serialVersionUID = 012345L;
	
	public ExternalSystemException(String message) {
		super(message);
	}

	public ExternalSystemException() {
		super();
	}

	public ExternalSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExternalSystemException(Throwable cause) {
		super(cause);
	}
}
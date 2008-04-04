package com.drey.aramarok.domain.exceptions;

public class FatalDomainException extends DomainException {

	private static final long serialVersionUID = 21;

	public FatalDomainException() {
	}
	
	public FatalDomainException(String message) {
		super(message);
	}	

	public FatalDomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public FatalDomainException(Throwable cause) {
		super(cause);
	}
}

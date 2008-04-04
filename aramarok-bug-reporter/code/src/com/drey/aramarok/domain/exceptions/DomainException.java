package com.drey.aramarok.domain.exceptions;

public class DomainException extends Exception {
	
	private static final long serialVersionUID = 1;	
	
	public DomainException() {
		super();
	}
	
	public DomainException(String message) {
		super(message);
	}

	public DomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public DomainException(Throwable cause) {
		super(cause);
	}
}

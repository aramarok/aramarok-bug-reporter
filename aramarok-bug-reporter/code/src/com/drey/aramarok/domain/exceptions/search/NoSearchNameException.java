package com.drey.aramarok.domain.exceptions.search;


public class NoSearchNameException extends SearchException {
	private static final long serialVersionUID = 9087562;

	public NoSearchNameException() {
		super();
	}

	public NoSearchNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSearchNameException(String message) {
		super(message);
	}

	public NoSearchNameException(Throwable cause) {
		super(cause);
	}
}
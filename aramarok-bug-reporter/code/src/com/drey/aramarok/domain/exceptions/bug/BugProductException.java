package com.drey.aramarok.domain.exceptions.bug;

public class BugProductException extends BugException{
	private static final long serialVersionUID = 1234321;

	public BugProductException() {
		super();
	}

	public BugProductException(String message, Throwable cause) {
		super(message, cause);
	}

	public BugProductException(String message) {
		super(message);
	}

	public BugProductException(Throwable cause) {
		super(cause);
	}
}
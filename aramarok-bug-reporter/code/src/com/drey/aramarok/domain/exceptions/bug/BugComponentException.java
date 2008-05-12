package com.drey.aramarok.domain.exceptions.bug;

public class BugComponentException extends BugException{
	private static final long serialVersionUID = 1234321;

	public BugComponentException() {
		super();
	}

	public BugComponentException(String message, Throwable cause) {
		super(message, cause);
	}

	public BugComponentException(String message) {
		super(message);
	}

	public BugComponentException(Throwable cause) {
		super(cause);
	}
}
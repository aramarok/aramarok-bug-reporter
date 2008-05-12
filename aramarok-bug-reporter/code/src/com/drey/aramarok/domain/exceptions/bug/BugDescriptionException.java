package com.drey.aramarok.domain.exceptions.bug;

public class BugDescriptionException extends BugException{
	private static final long serialVersionUID = 1234321;

	public BugDescriptionException() {
		super();
	}

	public BugDescriptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public BugDescriptionException(String message) {
		super(message);
	}

	public BugDescriptionException(Throwable cause) {
		super(cause);
	}
}
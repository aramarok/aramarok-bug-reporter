package com.drey.aramarok.domain.exceptions.bug;

public class BugSeverityException extends BugException{
	private static final long serialVersionUID = 1234321;

	public BugSeverityException() {
		super();
	}

	public BugSeverityException(String message, Throwable cause) {
		super(message, cause);
	}

	public BugSeverityException(String message) {
		super(message);
	}

	public BugSeverityException(Throwable cause) {
		super(cause);
	}
}
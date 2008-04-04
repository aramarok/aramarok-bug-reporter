package com.drey.aramarok.domain.exceptions.bug;

public class BugStatusChangeException extends BugException{
	private static final long serialVersionUID = 1234321;

	public BugStatusChangeException() {
		super();
	}

	public BugStatusChangeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BugStatusChangeException(String message) {
		super(message);
	}

	public BugStatusChangeException(Throwable cause) {
		super(cause);
	}
}
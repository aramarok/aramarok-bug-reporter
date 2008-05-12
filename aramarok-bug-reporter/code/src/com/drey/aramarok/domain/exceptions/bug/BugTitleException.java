package com.drey.aramarok.domain.exceptions.bug;

public class BugTitleException extends BugException{
	private static final long serialVersionUID = 1234321;

	public BugTitleException() {
		super();
	}

	public BugTitleException(String message, Throwable cause) {
		super(message, cause);
	}

	public BugTitleException(String message) {
		super(message);
	}

	public BugTitleException(Throwable cause) {
		super(cause);
	}
}
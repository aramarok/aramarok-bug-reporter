package com.drey.aramarok.domain.exceptions.bug;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class BugException extends NonFatalDomainException {
	private static final long serialVersionUID = 9087562;

	public BugException() {
		super();
	}

	public BugException(String message, Throwable cause) {
		super(message, cause);
	}

	public BugException(String message) {
		super(message);
	}

	public BugException(Throwable cause) {
		super(cause);
	}
}
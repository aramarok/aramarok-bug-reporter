package com.drey.aramarok.domain.exceptions.user;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class UserException  extends NonFatalDomainException {
	private static final long serialVersionUID = 9087562;

	public UserException() {
		super();
	}

	public UserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserException(String message) {
		super(message);
	}

	public UserException(Throwable cause) {
		super(cause);
	}
}
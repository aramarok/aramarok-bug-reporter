package com.drey.aramarok.domain.exceptions.user;

public class UserHasNoRightException  extends UserException {
	private static final long serialVersionUID = 9087562;

	public UserHasNoRightException() {
		super();
	}

	public UserHasNoRightException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserHasNoRightException(String message) {
		super(message);
	}

	public UserHasNoRightException(Throwable cause) {
		super(cause);
	}
}
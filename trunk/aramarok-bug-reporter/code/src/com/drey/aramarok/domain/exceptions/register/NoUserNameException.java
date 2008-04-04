package com.drey.aramarok.domain.exceptions.register;

public class NoUserNameException  extends RegisterException {
	private static final long serialVersionUID = 87654321;

	public NoUserNameException() {
		super();
	}

	public NoUserNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoUserNameException(String message) {
		super(message);
	}

	public NoUserNameException(Throwable cause) {
		super(cause);
	}
}

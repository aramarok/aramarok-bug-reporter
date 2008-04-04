package com.drey.aramarok.domain.exceptions.component;

public class ComponentNameAlreadyExistsException extends ComponentException {
	private static final long serialVersionUID = 9087562;

	public ComponentNameAlreadyExistsException() {
		super();
	}

	public ComponentNameAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentNameAlreadyExistsException(String message) {
		super(message);
	}

	public ComponentNameAlreadyExistsException(Throwable cause) {
		super(cause);
	}
}
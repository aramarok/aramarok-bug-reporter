package com.drey.aramarok.domain.exceptions.component;

public class ComponentNotFoundException extends ProductComponentException {
	private static final long serialVersionUID = 9087562;

	public ComponentNotFoundException() {
		super();
	}

	public ComponentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentNotFoundException(String message) {
		super(message);
	}

	public ComponentNotFoundException(Throwable cause) {
		super(cause);
	}
}
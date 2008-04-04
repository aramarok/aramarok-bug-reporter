package com.drey.aramarok.domain.exceptions.component;

public class NoComponentNameSpecifiedException extends ComponentException {
	private static final long serialVersionUID = 9087562;

	public NoComponentNameSpecifiedException() {
		super();
	}

	public NoComponentNameSpecifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoComponentNameSpecifiedException(String message) {
		super(message);
	}

	public NoComponentNameSpecifiedException(Throwable cause) {
		super(cause);
	}
}
package com.drey.aramarok.domain.exceptions.product;

public class NoProductNameSpecifiedException extends ProductException {
	private static final long serialVersionUID = 42236;

	public NoProductNameSpecifiedException() {
		super();
	}

	public NoProductNameSpecifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoProductNameSpecifiedException(String message) {
		super(message);
	}

	public NoProductNameSpecifiedException(Throwable cause) {
		super(cause);
	}
}

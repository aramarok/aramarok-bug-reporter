package com.drey.aramarok.domain.exceptions.product;

public class ProductNotFoundException extends ProductException {
	private static final long serialVersionUID = 34563;

	public ProductNotFoundException() {
		super();
	}

	public ProductNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProductNotFoundException(String message) {
		super(message);
	}

	public ProductNotFoundException(Throwable cause) {
		super(cause);
	}
}

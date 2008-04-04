package com.drey.aramarok.domain.exceptions.product;

public class ProductNameAlreadyExistsException extends ProductException {
	private static final long serialVersionUID = 152351;

	public ProductNameAlreadyExistsException() {
		super();
	}

	public ProductNameAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProductNameAlreadyExistsException(String message) {
		super(message);
	}

	public ProductNameAlreadyExistsException(Throwable cause) {
		super(cause);
	}
}
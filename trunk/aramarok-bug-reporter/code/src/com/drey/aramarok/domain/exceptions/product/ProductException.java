package com.drey.aramarok.domain.exceptions.product;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class ProductException extends NonFatalDomainException {
	private static final long serialVersionUID = 87562;

	public ProductException() {
		super();
	}

	public ProductException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProductException(String message) {
		super(message);
	}

	public ProductException(Throwable cause) {
		super(cause);
	}
}
package com.drey.aramarok.domain.exceptions.component;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class ProductComponentException extends NonFatalDomainException {
	private static final long serialVersionUID = 9087562;

	public ProductComponentException() {
		super();
	}

	public ProductComponentException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProductComponentException(String message) {
		super(message);
	}

	public ProductComponentException(Throwable cause) {
		super(cause);
	}
}
package com.drey.aramarok.domain.exceptions.component;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class ComponentException extends NonFatalDomainException {
	private static final long serialVersionUID = 9087562;

	public ComponentException() {
		super();
	}

	public ComponentException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentException(String message) {
		super(message);
	}

	public ComponentException(Throwable cause) {
		super(cause);
	}
}
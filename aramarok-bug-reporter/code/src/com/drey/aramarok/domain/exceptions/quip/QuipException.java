package com.drey.aramarok.domain.exceptions.quip;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class QuipException  extends NonFatalDomainException {
	private static final long serialVersionUID = 9087562;

	public QuipException() {
		super();
	}

	public QuipException(String message, Throwable cause) {
		super(message, cause);
	}

	public QuipException(String message) {
		super(message);
	}

	public QuipException(Throwable cause) {
		super(cause);
	}
}
package com.drey.aramarok.domain.exceptions.search;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class SearchException extends NonFatalDomainException {
	private static final long serialVersionUID = 9087562;

	public SearchException() {
		super();
	}

	public SearchException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchException(String message) {
		super(message);
	}

	public SearchException(Throwable cause) {
		super(cause);
	}
}
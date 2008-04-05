package com.drey.aramarok.domain.exceptions.version;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class ComponentVersionException extends NonFatalDomainException {
	private static final long serialVersionUID = 9087562;

	public ComponentVersionException() {
		super();
	}

	public ComponentVersionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentVersionException(String message) {
		super(message);
	}

	public ComponentVersionException(Throwable cause) {
		super(cause);
	}
}
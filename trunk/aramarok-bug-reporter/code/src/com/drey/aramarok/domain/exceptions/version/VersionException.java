package com.drey.aramarok.domain.exceptions.version;

import com.drey.aramarok.domain.exceptions.NonFatalDomainException;

public class VersionException extends NonFatalDomainException {
	private static final long serialVersionUID = 9087562;

	public VersionException() {
		super();
	}

	public VersionException(String message, Throwable cause) {
		super(message, cause);
	}

	public VersionException(String message) {
		super(message);
	}

	public VersionException(Throwable cause) {
		super(cause);
	}
}
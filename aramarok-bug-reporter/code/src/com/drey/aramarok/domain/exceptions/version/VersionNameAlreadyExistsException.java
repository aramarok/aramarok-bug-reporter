package com.drey.aramarok.domain.exceptions.version;

public class VersionNameAlreadyExistsException  extends VersionException {
	private static final long serialVersionUID = 90875322;

	public VersionNameAlreadyExistsException() {
		super();
	}

	public VersionNameAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public VersionNameAlreadyExistsException(String message) {
		super(message);
	}

	public VersionNameAlreadyExistsException(Throwable cause) {
		super(cause);
	}
}
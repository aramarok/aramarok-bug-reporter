package com.drey.aramarok.domain.exceptions.version;

public class VersionNotFoundException  extends ComponentVersionException {
	private static final long serialVersionUID = 9087562;

	public VersionNotFoundException() {
		super();
	}

	public VersionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public VersionNotFoundException(String message) {
		super(message);
	}

	public VersionNotFoundException(Throwable cause) {
		super(cause);
	}
}
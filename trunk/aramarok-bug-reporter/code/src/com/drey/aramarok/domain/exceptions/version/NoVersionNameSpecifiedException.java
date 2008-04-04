package com.drey.aramarok.domain.exceptions.version;

public class NoVersionNameSpecifiedException extends VersionException {
	private static final long serialVersionUID = 903487562;

	public NoVersionNameSpecifiedException() {
		super();
	}

	public NoVersionNameSpecifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoVersionNameSpecifiedException(String message) {
		super(message);
	}

	public NoVersionNameSpecifiedException(Throwable cause) {
		super(cause);
	}
}
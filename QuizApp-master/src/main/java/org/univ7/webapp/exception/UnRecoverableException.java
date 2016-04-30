package org.univ7.webapp.exception;

public class UnRecoverableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnRecoverableException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnRecoverableException(String message) {
		super(message);
	}
}

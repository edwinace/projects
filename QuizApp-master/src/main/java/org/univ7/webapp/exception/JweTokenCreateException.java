package org.univ7.webapp.exception;

public class JweTokenCreateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JweTokenCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	public JweTokenCreateException(String message) {
		super(message);
	}
}

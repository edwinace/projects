package org.univ7.webapp.exception;

public class JweTokenDecryptException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JweTokenDecryptException(String message, Throwable cause) {
		super(message, cause);
	}

	public JweTokenDecryptException(String message) {
		super(message);
	}
}

package br.unb.cic.goda.exception;

import org.springframework.http.HttpStatus;

public class ResponseException extends RuntimeException {

	private static final long serialVersionUID = -4273871591225466493L;

	private HttpStatus status = HttpStatus.BAD_REQUEST;

	public ResponseException(String message) {
		super(message);
	}

	public ResponseException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public ResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResponseException(String message, Throwable cause, HttpStatus status) {
		super(message, cause);
		this.status = status;
	}

	public ResponseException(Throwable cause, HttpStatus status) {
		super(cause.getMessage());
		this.status = status;
	}

	public ResponseException(Throwable cause) {
		super(cause.getMessage());
	}

	public HttpStatus getStatus() {
		return status;
	}
}

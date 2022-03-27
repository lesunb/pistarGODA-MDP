package br.unb.cic.goda.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;

@ControllerAdvice
public class ResponseExceptionHandler {

	@ExceptionHandler(value = { ResponseException.class })
	public ResponseEntity<Object> handleResponseException(ResponseException exc) {
		ResponseApi response = new ResponseApi(exc.getMessage(), exc.getCause(), exc.getStatus());

		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	public class ResponseApi {
//		private  HttpMethod method;
		private String message;
		@JsonIgnore
		private Throwable throwable;
		private int status;
		@JsonIgnore
		private Date date;

		public ResponseApi(String message, Throwable throwable, HttpStatus status) {
			this.message = message;
			this.throwable = throwable;
			this.status = status.value();
			this.date = new Date();
		}

		public String getMessage() {
			return message;
		}

		public Date getDate() {
			return date;
		}

		public Throwable getThrowable() {
			return throwable;
		}

		public int getStatus() {
			return status;
		}

	}
}
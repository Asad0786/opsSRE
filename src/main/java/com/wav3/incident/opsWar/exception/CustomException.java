package com.wav3.incident.opsWar.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -8687035865946883808L;
	private String message;
	private HttpStatus status = HttpStatus.BAD_REQUEST;

	public CustomException(String message) {
		super(message);
		this.message = message;
	}

	public CustomException(String message, HttpStatus status) {
		super(message);
		this.message = message;
		this.status = status;
	}

}

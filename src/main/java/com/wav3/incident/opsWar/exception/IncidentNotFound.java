package com.wav3.incident.opsWar.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidentNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4995002865062127869L;
	private String message;

	public IncidentNotFound(String message) {
		// TODO Auto-generated constructor stub
		super(message);
		this.message = message;
	}

}
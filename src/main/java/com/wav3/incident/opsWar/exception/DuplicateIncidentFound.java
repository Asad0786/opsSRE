package com.wav3.incident.opsWar.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateIncidentFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9033479033820127611L;
	private String message;

	public DuplicateIncidentFound(String message) {
		// TODO Auto-generated constructor stub
		super(message);
		this.message = message;
	}

}

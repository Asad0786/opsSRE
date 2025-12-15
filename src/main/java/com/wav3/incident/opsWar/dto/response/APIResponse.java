package com.wav3.incident.opsWar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {

	private String message;
	private String uri;
	private Object data;
	private Object errors;
	private int statusCode;
	private int errorCode;
}
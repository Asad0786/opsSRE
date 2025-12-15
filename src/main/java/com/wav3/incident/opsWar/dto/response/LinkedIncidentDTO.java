package com.wav3.incident.opsWar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkedIncidentDTO {

	private Long id;
	private String referenceCode;
	private String artifactType;
}

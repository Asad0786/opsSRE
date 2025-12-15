package com.wav3.incident.opsWar.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogSnippetResponse extends IncidentResponseDTO {

	private String rawLogContent;
	private String serverIp;
}

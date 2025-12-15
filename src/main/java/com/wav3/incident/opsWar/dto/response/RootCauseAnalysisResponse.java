package com.wav3.incident.opsWar.dto.response;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RootCauseAnalysisResponse extends IncidentResponseDTO {

	private String summary;
	private Set<IncidentResponseDTO> linkedArtifacts;
}

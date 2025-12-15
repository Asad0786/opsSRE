package com.wav3.incident.opsWar.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootCauseAnalysisRequestDTO extends IncidentRequestDTO {

	@NotBlank(message = "Summary Cannot be empty")
	private String summary;
	@NotEmpty(message = "Linked Incidents ids cannot be empty")
	private List<Long> linkedIncidentIds;

}

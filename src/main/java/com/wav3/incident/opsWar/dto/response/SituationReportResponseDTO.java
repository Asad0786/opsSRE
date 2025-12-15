package com.wav3.incident.opsWar.dto.response;

import com.wav3.incident.opsWar.models.SituationReport.Severity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SituationReportResponseDTO extends IncidentResponseDTO {

	private String statusMessage;
	private Severity severity;
}

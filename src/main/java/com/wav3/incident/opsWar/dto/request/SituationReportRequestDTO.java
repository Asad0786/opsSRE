package com.wav3.incident.opsWar.dto.request;

import com.wav3.incident.opsWar.models.SituationReport.Severity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SituationReportRequestDTO extends IncidentRequestDTO {

	@NotBlank(message = "status message cannot be empty")
	@Size(min = 10, message = "status message should be of atleast 10 characters")
	private String statusMessage;

	@NotNull(message = "Severity is Required")
	private Severity severity;

}

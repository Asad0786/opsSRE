package com.wav3.incident.opsWar.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "incidentType")
@JsonSubTypes({ @JsonSubTypes.Type(value = SituationReportRequestDTO.class, name = "SITUATION_REPORT"),
		@JsonSubTypes.Type(value = MonitoringDashboardRequestDTO.class, name = "MONITORING_DASHBOARD"),
		@JsonSubTypes.Type(value = LogSnippetRequestDTO.class, name = "LOG_SNIPPET"),
		@JsonSubTypes.Type(value = RootCauseAnalysisRequestDTO.class, name = "ROOT_CAUSE_ANALYSIS") })
public abstract class IncidentRequestDTO {

	@NotBlank(message = "Reference Code cannot be blank")
	@Size(min = 1, max = 100, message = "Reference Code must be between 1 and 100 characters")
	private String referenceCode;
	@NotBlank(message = "incidentId cannot be blank")
	@Size(min = 1, max = 50, message = "incidentId must be between 1 and 50 characters")
	private String incidentId;

}

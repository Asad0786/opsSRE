package com.wav3.incident.opsWar.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "incidentType")
@JsonSubTypes({ @JsonSubTypes.Type(value = SituationReportResponseDTO.class, name = "SITUATION_REPORT"),
		@JsonSubTypes.Type(value = MonitoringDashboardResponse.class, name = "MONITORING_DASHBOARD"),
		@JsonSubTypes.Type(value = LogSnippetResponse.class, name = "LOG_SNIPPET"),
		@JsonSubTypes.Type(value = RootCauseAnalysisResponse.class, name = "ROOT_CAUSE_ANALYSIS") })
public abstract class IncidentResponseDTO {

	private Long id;
	private String referenceCode;
	private String incidentId;
	private LocalDate createdDate;
	private String incidentType;
}

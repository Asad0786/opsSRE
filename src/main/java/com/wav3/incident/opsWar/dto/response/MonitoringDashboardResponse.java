package com.wav3.incident.opsWar.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MonitoringDashboardResponse extends IncidentResponseDTO {
	private String graphUrl;
	private String toolName;
}

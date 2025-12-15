package com.wav3.incident.opsWar.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonitoringDashboardRequestDTO extends IncidentRequestDTO {

	@URL
	@Size(min = 5, max = 255, message = "URL should be between 5 to 255 characters")
	private String graphUrl;
	@Size(min = 2, max = 150, message = "Tool name should be of vetween 2 to 200 character long")
	private String toolName;
}

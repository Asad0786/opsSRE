package com.wav3.incident.opsWar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogSnippetRequestDTO extends IncidentRequestDTO {

	@NotBlank(message = "Raw Log Content cannot be empty")
	@Size(min = 5, message = "Raw content cannot be less than 5 character")
	private String rawLogContent;
	@NotBlank
	@Size(min = 6, max = 20, message = "Invalid ip address")
	private String serverIp;

}

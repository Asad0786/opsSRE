package com.wav3.incident.opsWar.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@DiscriminatorValue("LOG_SNIPPET")
@Getter
@Setter
public class LogSnippet extends Incident {

	@Column(columnDefinition = "LONGTEXT")
	private String rawLogContent;
	@Column(length = 20)
	private String serverIp;

}

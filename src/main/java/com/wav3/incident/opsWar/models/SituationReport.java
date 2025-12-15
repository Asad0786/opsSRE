package com.wav3.incident.opsWar.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "situation_report")
@DiscriminatorValue(value = "SITUATION_REPORT")
@Getter
@Setter
public class SituationReport extends Incident {

	@Column(columnDefinition = "LONGTEXT")
	private String statusMessage;

	@Enumerated(EnumType.STRING)
	private Severity severity;

	public enum Severity {
		INFO, CRITICAL
	}

}

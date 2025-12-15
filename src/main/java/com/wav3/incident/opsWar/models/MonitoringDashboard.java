package com.wav3.incident.opsWar.models;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "monitoring_dashboard")
@DiscriminatorValue(value = "MONITORING_DASHBOARD")
@Getter
@Setter
public class MonitoringDashboard extends Incident {

	@URL
	private String graphUrl;
	@Column(length = 100)
	private String toolName;

}

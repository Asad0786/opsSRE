package com.wav3.incident.opsWar.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "root_cause_analysis")
@DiscriminatorValue("ROOT_CAUSE_ANALYSIS")
@Getter
@Setter
public class RootCauseAnalysis extends Incident {

	@Column(columnDefinition = "LONGTEXT")
	private String summary;

	@ManyToMany
	@JoinTable(name = "rca_incidents", joinColumns = @JoinColumn(name = "rca_id"), inverseJoinColumns = @JoinColumn(name = "incident_id"))
	private List<Incident> linkedIncidents;

}

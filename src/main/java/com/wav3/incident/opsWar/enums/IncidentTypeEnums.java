package com.wav3.incident.opsWar.enums;

import com.wav3.incident.opsWar.models.Incident;
import com.wav3.incident.opsWar.models.LogSnippet;
import com.wav3.incident.opsWar.models.MonitoringDashboard;
import com.wav3.incident.opsWar.models.RootCauseAnalysis;
import com.wav3.incident.opsWar.models.SituationReport;

public enum IncidentTypeEnums {
	SITUATION_REPORT, MONITORING_DASHBOARD, LOG_SNIPPET, ROOT_CAUSE_ANALYSIS;

	public Class<? extends Incident> toEntity() {
		return switch (this) {
		case SITUATION_REPORT -> SituationReport.class;
		case MONITORING_DASHBOARD -> MonitoringDashboard.class;
		case LOG_SNIPPET -> LogSnippet.class;
		case ROOT_CAUSE_ANALYSIS -> RootCauseAnalysis.class;
		default -> null;
		};
	}

}

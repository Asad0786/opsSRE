package com.wav3.incident.opsWar.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.wav3.incident.opsWar.dto.request.IncidentRequestDTO;
import com.wav3.incident.opsWar.dto.request.LogSnippetRequestDTO;
import com.wav3.incident.opsWar.dto.request.MonitoringDashboardRequestDTO;
import com.wav3.incident.opsWar.dto.request.RootCauseAnalysisRequestDTO;
import com.wav3.incident.opsWar.dto.request.SituationReportRequestDTO;
import com.wav3.incident.opsWar.dto.response.IncidentResponseDTO;
import com.wav3.incident.opsWar.dto.response.LogSnippetResponse;
import com.wav3.incident.opsWar.dto.response.MonitoringDashboardResponse;
import com.wav3.incident.opsWar.dto.response.RootCauseAnalysisResponse;
import com.wav3.incident.opsWar.dto.response.SituationReportResponseDTO;
import com.wav3.incident.opsWar.models.Incident;
import com.wav3.incident.opsWar.models.LogSnippet;
import com.wav3.incident.opsWar.models.MonitoringDashboard;
import com.wav3.incident.opsWar.models.RootCauseAnalysis;
import com.wav3.incident.opsWar.models.SituationReport;

import jakarta.persistence.DiscriminatorValue;

public class IncidentMapper {

	public static Incident toEntity(IncidentRequestDTO dto) {

		if (dto instanceof SituationReportRequestDTO r) {
			SituationReport e = new SituationReport();
			mapBase(dto, e);
			e.setStatusMessage(r.getStatusMessage());
			e.setSeverity(r.getSeverity());
			return e;
		}

		if (dto instanceof MonitoringDashboardRequestDTO r) {
			MonitoringDashboard e = new MonitoringDashboard();
			mapBase(dto, e);
			e.setGraphUrl(r.getGraphUrl());
			e.setToolName(r.getToolName());
			return e;
		}

		if (dto instanceof LogSnippetRequestDTO r) {
			LogSnippet e = new LogSnippet();
			mapBase(dto, e);
			e.setRawLogContent(r.getRawLogContent());
			e.setServerIp(r.getServerIp());
			return e;
		}

		if (dto instanceof RootCauseAnalysisRequestDTO r) {
			RootCauseAnalysis e = new RootCauseAnalysis();
			mapBase(dto, e);
			e.setSummary(r.getSummary());
			return e;
		}

		return null;
	}

	public static IncidentResponseDTO toResponse(Incident entity, boolean nestedChild) {
		if (entity instanceof SituationReport e) {
			SituationReportResponseDTO r = new SituationReportResponseDTO();
			mapBaseResponse(entity, r);
			r.setStatusMessage(e.getStatusMessage());
			r.setSeverity(e.getSeverity());
			return r;
		}

		if (entity instanceof MonitoringDashboard e) {
			MonitoringDashboardResponse r = new MonitoringDashboardResponse();
			mapBaseResponse(entity, r);
			r.setGraphUrl(e.getGraphUrl());
			r.setToolName(e.getToolName());
			return r;
		}

		if (entity instanceof LogSnippet e) {
			LogSnippetResponse r = new LogSnippetResponse();
			mapBaseResponse(entity, r);
			r.setRawLogContent(e.getRawLogContent());
			r.setServerIp(e.getServerIp());
			return r;
		}

		if (entity instanceof RootCauseAnalysis e) {
			RootCauseAnalysisResponse r = new RootCauseAnalysisResponse();
			mapBaseResponse(entity, r);
			r.setSummary(e.getSummary());
			if (nestedChild) {
				return r;
			}
			Set<IncidentResponseDTO> children = e.getLinkedIncidents().stream().map(child -> toResponse(child, true))
					.collect(Collectors.toSet());
			r.setLinkedArtifacts(children);

			return r;
		}

		return null;
	}

	public static void updateEntity(IncidentRequestDTO dto, Incident entity) {

		mapBase(dto, entity);

		if (dto instanceof SituationReportRequestDTO r && entity instanceof SituationReport e) {
			e.setStatusMessage(r.getStatusMessage());
			e.setSeverity(r.getSeverity());
		}

		if (dto instanceof MonitoringDashboardRequestDTO r && entity instanceof MonitoringDashboard e) {
			e.setGraphUrl(r.getGraphUrl());
			e.setToolName(r.getToolName());
		}

		if (dto instanceof LogSnippetRequestDTO r && entity instanceof LogSnippet e) {
			e.setRawLogContent(r.getRawLogContent());
			e.setServerIp(r.getServerIp());
		}

		if (dto instanceof RootCauseAnalysisRequestDTO r && entity instanceof RootCauseAnalysis e) {
			e.setSummary(r.getSummary());
		}
	}

	private static void mapBase(IncidentRequestDTO src, Incident tgt) {
		tgt.setReferenceCode(src.getReferenceCode());
		tgt.setIncidentId(src.getIncidentId());
	}

	private static void mapBaseResponse(Incident src, IncidentResponseDTO tgt) {
		tgt.setId(src.getId());
		tgt.setReferenceCode(src.getReferenceCode());
		tgt.setIncidentId(src.getIncidentId());
		tgt.setCreatedDate(src.getCreatedDate());
		tgt.setIncidentType(resolveDiscriminatorValue(src));
	}

	// Get Incident for parent entry
	private static String resolveDiscriminatorValue(Incident incident) {
		DiscriminatorValue dv = incident.getClass().getAnnotation(DiscriminatorValue.class);
		return dv != null ? dv.value() : null;
	}
}

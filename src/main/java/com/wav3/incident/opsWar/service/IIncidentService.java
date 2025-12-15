package com.wav3.incident.opsWar.service;

import com.wav3.incident.opsWar.dto.request.IncidentPaginationRequestDTO;
import com.wav3.incident.opsWar.dto.request.IncidentRequestDTO;
import com.wav3.incident.opsWar.dto.response.IncidentResponseDTO;
import com.wav3.incident.opsWar.dto.response.PaginatedResponse;

public interface IIncidentService {

	IncidentResponseDTO createIncident(IncidentRequestDTO request);

	PaginatedResponse<IncidentResponseDTO> getAllIncidents(IncidentPaginationRequestDTO filter);

	IncidentResponseDTO getIncidentById(Long id);

	void deleteIncident(Long id);
	
	IncidentResponseDTO update(Long id, IncidentRequestDTO request);

}

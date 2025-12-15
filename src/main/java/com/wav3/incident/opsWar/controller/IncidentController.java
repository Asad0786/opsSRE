package com.wav3.incident.opsWar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wav3.incident.opsWar.dto.request.IncidentPaginationRequestDTO;
import com.wav3.incident.opsWar.dto.request.IncidentRequestDTO;
import com.wav3.incident.opsWar.dto.response.APIResponse;
import com.wav3.incident.opsWar.dto.response.IncidentResponseDTO;
import com.wav3.incident.opsWar.dto.response.PaginatedResponse;
import com.wav3.incident.opsWar.service.IIncidentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class IncidentController {

	private final IIncidentService service;

	@PostMapping("/incident")
	public ResponseEntity<APIResponse> createIncident(@Valid @RequestBody IncidentRequestDTO request) {

		IncidentResponseDTO response = service.createIncident(request);
		APIResponse apiResponse = new APIResponse("Incident created successfully", "/api/incidents", response, null,
				HttpStatus.CREATED.value(), 0);
		log.info("#createIncident Incident::created successfully: id={}, referenceCode={}", response.getId(),
				response.getReferenceCode());
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@PutMapping("/incident/{id}")
	public ResponseEntity<APIResponse> updateIncident(@PathVariable Long id,
			@Valid @RequestBody IncidentRequestDTO request) {

		log.info("#updateIncident::Update Incident request received: id={}", id);

		IncidentResponseDTO response = service.update(id, request);

		APIResponse apiResponse = new APIResponse("Incident updated successfully", "/api/incidents/" + id, response,
				null, HttpStatus.OK.value(), 0);

		log.info("#updateIncident::Incident updated successfully: id={}", id);

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/incident/{id}")
	public ResponseEntity<APIResponse> getById(@PathVariable Long id) {

		log.info("#getIncidentById::request received: id={}", id);

		IncidentResponseDTO response = service.getIncidentById(id);

		APIResponse apiResponse = new APIResponse("Incident fetched successfully", "/api/incidents/" + id, response,
				null, HttpStatus.OK.value(), 0);

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/incidents")
	public ResponseEntity<APIResponse> getAllIncidents(@ModelAttribute @Valid IncidentPaginationRequestDTO filter) {
		log.info("#getAllIncidents request received: filters={}", filter.toString());
		PaginatedResponse<IncidentResponseDTO> response = service.getAllIncidents(filter);
		APIResponse apiResponse = new APIResponse("Incidents fetched successfully", "/api/incidents", response, null,
				HttpStatus.OK.value(), 0);
		return ResponseEntity.ok(apiResponse);
	}

	@DeleteMapping("/incident/{id}")
	public ResponseEntity<APIResponse> deleteIncident(@PathVariable Long id) {

		log.info("#deleteIncident request received: id={}", id);
		service.deleteIncident(id);
		APIResponse apiResponse = new APIResponse("Incident deleted successfully", "/api/incidents/" + id, null, null,
				HttpStatus.OK.value(), 0);

		log.info("#deleteIncident Incident deleted successfully: id={}", id);

		return ResponseEntity.ok(apiResponse);
	}
}

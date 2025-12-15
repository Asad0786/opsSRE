package com.wav3.incident.opsWar.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wav3.incident.opsWar.dto.request.IncidentPaginationRequestDTO;
import com.wav3.incident.opsWar.dto.request.IncidentRequestDTO;
import com.wav3.incident.opsWar.dto.request.RootCauseAnalysisRequestDTO;
import com.wav3.incident.opsWar.dto.response.IncidentResponseDTO;
import com.wav3.incident.opsWar.dto.response.PaginatedResponse;
import com.wav3.incident.opsWar.exception.CustomException;
import com.wav3.incident.opsWar.exception.DuplicateIncidentFound;
import com.wav3.incident.opsWar.exception.IncidentNotFound;
import com.wav3.incident.opsWar.mapper.IncidentMapper;
import com.wav3.incident.opsWar.models.Incident;
import com.wav3.incident.opsWar.models.RootCauseAnalysis;
import com.wav3.incident.opsWar.repository.IncidentRepository;
import com.wav3.incident.opsWar.service.IIncidentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class IncidentServiceImpl implements IIncidentService {

	private final IncidentRepository incidentRepository;

	@Override
	public IncidentResponseDTO createIncident(IncidentRequestDTO request) {
		log.info("#createIncident Create Incident request received: referenceCode={}", request.getReferenceCode());

		if (this.incidentRepository.existsByReferenceCode(request.getReferenceCode())) {
			log.info("#createIncident Duplicate request with refernce code={}", request.getReferenceCode());
			throw new DuplicateIncidentFound("An incident  with same reference codealready exists");
		}

		log.info("#createIncident Request is for {} type", request.getClass());
		Incident entity = IncidentMapper.toEntity(request);

		log.info("#createIncident Duplicate request with refernce code={}", request.getReferenceCode());
		if (entity instanceof RootCauseAnalysis rca && request instanceof RootCauseAnalysisRequestDTO r) {
			List<Incident> incidents = this.incidentRepository.findAllById(r.getLinkedIncidentIds());
			if (incidents.size() != r.getLinkedIncidentIds().size()) {
				throw new CustomException("One or more Incident with requested ID does not exists",
						HttpStatus.NOT_FOUND);

			}
			rca.setLinkedIncidents(incidents);
		}
		return IncidentMapper.toResponse(incidentRepository.save(entity), false);
	}

	@Override
	public PaginatedResponse<IncidentResponseDTO> getAllIncidents(IncidentPaginationRequestDTO filter) {

		Pageable pageable;
		int page = 1;
		if (filter.getPage() == -1) {
			pageable = Pageable.unpaged();
		} else {
			page = Math.max(filter.getPage(), 1);
			Sort sort = Sort.by(filter.getSort(), filter.getSortBy().name());
			pageable = PageRequest.of(page - 1, filter.getSize(), sort);
		}
		log.info("#getAllIncidents with filters={}, pageable={}", filter.toString(), pageable.toString());

		Class<? extends Incident> incidentClass = filter.getIncidentType() != null ? filter.getIncidentType().toEntity()
				: null;

		Page<Incident> incidentsPage = this.incidentRepository.findAllWithFilters(filter.getIncidentId(), incidentClass,
				filter.getReferenceCode(), filter.getStartDate(), filter.getEndDate(), pageable);
		log.info("#getAllIncidents found {} number of elements", incidentsPage.getContent().size());

		List<IncidentResponseDTO> incidents = incidentsPage.getContent().stream()
				.map(incident -> IncidentMapper.toResponse(incident, false)).collect(Collectors.toList());

		return new PaginatedResponse<IncidentResponseDTO>(incidents, page, filter.getSize(),
				incidentsPage.getTotalElements(), incidentsPage.getTotalPages(), incidentsPage.hasNext(),
				incidentsPage.hasPrevious());
	}

	@Override
	public IncidentResponseDTO getIncidentById(Long id) {

		log.info("#getIncidentById:: fetch incident for id {}", id);
		Incident incident = this.incidentRepository.findById(id).orElseThrow(() -> {
			log.info("#getIncidentById:: Incident does not exists with id {}", id);
			return new IncidentNotFound("Incident with " + id + " not found");
		});
		log.info("#getIncidentById:: incident found with Id {}", id);
		return IncidentMapper.toResponse(incident, false);
	}

	@Override
	public void deleteIncident(Long id) {

		log.info("#deleteIncident:: fetch incident for id {} beofore deleteing", id);
		Incident incident = this.incidentRepository.findById(id).orElseThrow(() -> {
			log.info("#deleteIncident:: Incident does not exists with id {}", id);
			return new IncidentNotFound("Incident with " + id + " not found");
		});
		log.info("#deleteIncident:: found incident for id {}, checking if root cause exists", id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))
				&& !auth.getName().equals(incident.getCreatedBy())) {
			throw new AccessDeniedException("You dont have enough permission to delete this incident");
		}

		List<RootCauseAnalysis> rootCauses = this.incidentRepository.findAllContainingIncident(id);

		log.info("#deleteIncident:: #rootCauses found {} root causes having incident id: {}", rootCauses.size(), id);

		for (RootCauseAnalysis rootCause : rootCauses) {
			log.info("#deleteIncident::removing incident {} from root cause {}", id, rootCause.getId());
			rootCause.getLinkedIncidents().remove(incident);
		}

		this.incidentRepository.delete(incident);

		return;

	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public IncidentResponseDTO update(Long id, IncidentRequestDTO request) {

		log.info("#updateIncident:: fetch incident for id {} beofore updating", id);
		Incident incident = this.incidentRepository.findById(id).orElseThrow(() -> {
			log.info("#updateIncident:: Incident does not exists with id {}", id);
			return new IncidentNotFound("Incident with id " + id + " not found");
		});
		if (this.incidentRepository.existsByReferenceCodeAndIdNot(request.getReferenceCode(), id)) {
			throw new DuplicateIncidentFound("An incident with same reference codealready exists");
		}
		if (incident instanceof RootCauseAnalysis rca) {
			rca.getLinkedIncidents().clear();
		}

		IncidentMapper.updateEntity(request, incident);
		if (incident instanceof RootCauseAnalysis rca && request instanceof RootCauseAnalysisRequestDTO r) {
			List<Incident> incidents = this.incidentRepository.findAllById(r.getLinkedIncidentIds());
			if (incidents.size() != r.getLinkedIncidentIds().size()) {
				throw new CustomException("One or more Incident with requested ID does not exists",
						HttpStatus.NOT_FOUND);

			}
			rca.setLinkedIncidents(incidents);
		}

		return IncidentMapper.toResponse(this.incidentRepository.save(incident), false);
	}

}

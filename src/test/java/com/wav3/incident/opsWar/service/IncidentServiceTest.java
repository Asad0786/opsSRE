package com.wav3.incident.opsWar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wav3.incident.opsWar.dto.response.IncidentResponseDTO;
import com.wav3.incident.opsWar.exception.CustomException;
import com.wav3.incident.opsWar.exception.IncidentNotFound;
import com.wav3.incident.opsWar.models.SituationReport;
import com.wav3.incident.opsWar.models.SituationReport.Severity;
import com.wav3.incident.opsWar.repository.IncidentRepository;
import com.wav3.incident.opsWar.serviceImpl.IncidentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceTest {

	@Mock
	private IncidentRepository repository;

	@InjectMocks
	private IncidentServiceImpl service;

	@Test
	void getByIdShouldReturnIncidentDTOWhenExists() throws CustomException {
		SituationReport incident = new SituationReport();
		incident.setId(1L);
		incident.setReferenceCode("Test");
		incident.setIncidentId("Test");
		incident.setSeverity(Severity.INFO);
		incident.setStatusMessage("Test");

		when(repository.findById(1L)).thenReturn(Optional.of(incident));

		IncidentResponseDTO result = service.getIncidentById(1L);
		assertThat(result).isNotNull();
		assertThat(result.getReferenceCode()).isEqualTo("Test");
	}

	@Test
	void getByIdShouldThrowExceptionWhenNotFound() {
		when(repository.findById(99L)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> service.getIncidentById(99L)).isInstanceOf(IncidentNotFound.class);
	}

}

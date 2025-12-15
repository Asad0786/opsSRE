package com.wav3.incident.opsWar.dto.request;

import java.time.LocalDate;

import org.springframework.data.domain.Sort;

import com.wav3.incident.opsWar.enums.IncidentTypeEnums;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IncidentPaginationRequestDTO {

	@Min(value = -1, message = "Page should be greater than or equal to -1, -1 only if you want all records")
	private int page = 0;
	@Min(value = 1, message = "Page should be greater than or equal to 1")
	private int size = 10;
	private String incidentId;
	private String referenceCode;
	private LocalDate startDate;
	private LocalDate endDate;
	private Sort.Direction sort = Sort.Direction.ASC;
	private SortBy sortBy = SortBy.id;

	private IncidentTypeEnums incidentType;

	public enum SortBy {
		id, createdDate
	}

}

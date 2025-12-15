package com.wav3.incident.opsWar.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wav3.incident.opsWar.models.Incident;
import com.wav3.incident.opsWar.models.RootCauseAnalysis;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

	boolean existsByReferenceCode(String referenceCode);

	@Query(value = """
			    SELECT i
			    FROM Incident i
			    WHERE (:incidentId IS NULL
			           OR LOWER(i.incidentId) LIKE  LOWER(CONCAT('%', :incidentId, '%')))
			      AND (:incidentType IS NULL
			           OR TYPE(i) = :incidentType)
			      AND (:referenceCode IS NULL
			           OR LOWER(i.referenceCode) = LOWER(:referenceCode))
			      AND (:startDate IS NULL
			           OR i.createdDate >= :startDate)
			      AND (:endDate IS NULL
			           OR i.createdDate <= :endDate)
			""", countQuery = """
			    SELECT COUNT(i)
			    FROM Incident i
			    WHERE (:incidentId IS NULL
			            OR LOWER(i.incidentId) LIKE LOWER(CONCAT('%', :incidentId, '%')))
			      AND (:incidentType IS NULL
			           OR TYPE(i) = :incidentType)
			      AND (:referenceCode IS NULL
			           OR LOWER(i.referenceCode) = LOWER(:referenceCode))
			      AND (:startDate IS NULL
			           OR i.createdDate >= :startDate)
			      AND (:endDate IS NULL
			           OR i.createdDate <= :endDate)
			""")
	Page<Incident> findAllWithFilters(@Param("incidentId") String incidentId,
			@Param("incidentType") Class<? extends Incident> incidentType, @Param("referenceCode") String referenceCode,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

	boolean existsByReferenceCodeAndIdNot(String referenceCode, Long id);

	@Query("""
			    SELECT r FROM RootCauseAnalysis r
			    JOIN r.linkedIncidents  i
			    WHERE i.id = :incidentId
			""")
	List<RootCauseAnalysis> findAllContainingIncident(@Param("incidentId") Long incidentId);

}

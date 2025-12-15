package com.wav3.incident.opsWar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wav3.incident.opsWar.models.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {


}

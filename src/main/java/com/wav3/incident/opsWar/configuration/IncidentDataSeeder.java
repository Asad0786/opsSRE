
package com.wav3.incident.opsWar.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wav3.incident.opsWar.models.Incident;
import com.wav3.incident.opsWar.models.LogSnippet;
import com.wav3.incident.opsWar.models.MonitoringDashboard;
import com.wav3.incident.opsWar.models.RootCauseAnalysis;
import com.wav3.incident.opsWar.models.SituationReport;
import com.wav3.incident.opsWar.models.SituationReport.Severity;
import com.wav3.incident.opsWar.repository.IncidentRepository;

@Configuration
public class IncidentDataSeeder {

	@Bean
	CommandLineRunner seedIncidents(IncidentRepository repository) {
		return args -> {

			if (repository.count() > 0)
				return;

			Random r = new Random();
			List<Incident> all = new ArrayList<>();

			for (int i = 1; i <= 20; i++) {

				Incident incident;

				switch (r.nextInt(4)) {

				case 0 -> {
					SituationReport sr = new SituationReport();
					sr.setReferenceCode("SIT-" + i);
					sr.setIncidentId("INC-2001");
					sr.setStatusMessage("SITUAUTION REPORT " + i);
					sr.setCreatedBy("user");
					sr.setSeverity(r.nextBoolean() ? Severity.CRITICAL : Severity.INFO);
					incident = sr;
				}

				case 1 -> {
					MonitoringDashboard md = new MonitoringDashboard();
					md.setReferenceCode("MON-" + i);
					md.setIncidentId("INC-2001");
					md.setCreatedBy("user");
					md.setGraphUrl("https://katbin.log/somelogs/" + i);
					md.setToolName("Grafana");
					incident = md;
				}

				case 2 -> {
					LogSnippet log = new LogSnippet();
					log.setReferenceCode("LOG-" + i);
					log.setIncidentId("INC-2001");
					log.setRawLogContent("Timeout on node " + i + " fails with an uexpected exception");
					log.setCreatedBy("user");
					log.setServerIp("10.0.0." + r.nextInt(255));
					incident = log;
				}

				default -> {
					RootCauseAnalysis rca = new RootCauseAnalysis();
					rca.setReferenceCode("RCA-" + i);
					rca.setIncidentId("INC-2001");
					rca.setCreatedBy("user");
					rca.setSummary("Resource exhaustions " + i);
					if (!all.isEmpty()) {
						rca.setLinkedIncidents(all.subList(0, Math.min(3, all.size())));
					}
					incident = rca;
				}
				}

				all.add(repository.save(incident));
			}
		};
	}
}

package com.wav3.incident.opsWar.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class IncidentTestController {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void createResourceShouldWorkWhenAuthenticated() throws Exception {
		mockMvc.perform(post("/api/incident").with(httpBasic("user", "user")).contentType(MediaType.APPLICATION_JSON)
				.content("""
						    {
						      "incidentType": "SITUATION_REPORT",
						      "referenceCode": "SRE-001",
						      "incidentId": "INC-15-12-2025",
						      "statusMessage":"DB stacked",
						      "severity":"CRITICAL"
						    }
						""")).andExpect(status().isCreated()).andDo(h -> {
					System.out.println(h);
				}).andExpect(jsonPath("$.data.id").exists())
				.andExpect(jsonPath("$.data.referenceCode").value("SRE-001"));
	}

	@Test
	void listResourcesShouldBeAccessibleWithoutAuth() throws Exception {
		mockMvc.perform(get("/api/incidents")).andExpect(status().isOk());
	}

}

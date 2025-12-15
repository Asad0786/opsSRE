package com.wav3.incident.opsWar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "securityAuditor")
public class OpsWarApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpsWarApplication.class, args);
	}

}

package com.wav3.incident.opsWar.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "appinmemory")
public class InMemoryUserProperties {
	private List<UserConfig> users;

	@Getter
	@Setter
	public static class UserConfig {
		private String role;
		private String username;
		private String password;
	}
}

package com.evluhin.nest.dto;

/**
 */
public class SettingsDto {
	private String authUrl;

	public SettingsDto() {
	}

	public SettingsDto(String authUrl) {
		this.authUrl = authUrl;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
}

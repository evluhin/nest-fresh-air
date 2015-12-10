package com.evluhin.nest;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 */
@ConfigurationProperties("nest.product")
public class NestProperties {
	/**
	 * Product id.
	 */
	private String id;

	/**
	 * Product secret.
	 */
	private String secret;
	private String authUrl;
	private String tokenUrl;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String rawTokenUrl) {
		this.tokenUrl = rawTokenUrl;
	}
}

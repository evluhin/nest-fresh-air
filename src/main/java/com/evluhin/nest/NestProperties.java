package com.evluhin.nest;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.text.MessageFormat;

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
	private String authUrlTemplate;
	private String tokenUrlTemplate;
	private String rawTokenUrl;

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

	public String getAuthUrlTemplate() {
		return authUrlTemplate;
	}

	public void setAuthUrlTemplate(String authUrlTemplate) {
		this.authUrlTemplate = authUrlTemplate;
	}

	public String getTokenUrlTemplate() {
		return tokenUrlTemplate;
	}

	public void setTokenUrlTemplate(String tokenUrlTemplate) {
		this.tokenUrlTemplate = tokenUrlTemplate;
	}

	public String getAuthUrl() {
		return MessageFormat.format(getAuthUrlTemplate(), getId());
	}

	public String getTokenUrl(String code) {
		return MessageFormat.format(getTokenUrlTemplate(), code);
	}

	public String getRawTokenUrl() {
		return rawTokenUrl;
	}

	public void setRawTokenUrl(String rawTokenUrl) {
		this.rawTokenUrl = rawTokenUrl;
	}
}

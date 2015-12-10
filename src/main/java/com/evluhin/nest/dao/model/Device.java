package com.evluhin.nest.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.MappedSuperclass;

/**
 */

@MappedSuperclass
public class Device extends NestEntity {

	private String location;

	public Device() {
	}

	public Device(String id) {
		super(id);
	}

	public Device(String id, String name) {
		super(id, name);
	}

	@JsonProperty("where_id")
	public String getLocation() {
		return location;
	}

	@JsonProperty("where_id")
	public void setLocation(String location) {
		this.location = location;
	}

	@JsonProperty("device_id")
	@Override
	public String getId() {
		return super.getId();
	}

	@JsonProperty("device_id")
	@Override
	public void setId(String id) {
		super.setId(id);
	}
}

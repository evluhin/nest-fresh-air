package com.evluhin.nest.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 */
@Entity
@Table
public class Thermostat extends Device {

	private String mode;
	private boolean canHeat;
	private boolean canCool;

	public Thermostat() {
	}

	public Thermostat(String id) {
		super(id);
	}

	@JsonProperty("hvac_mode")
	public String getMode() {
		return mode;
	}

	@JsonProperty("hvac_mode")
	public void setMode(String mode) {
		this.mode = mode;
	}

	@JsonProperty("can_heat")

	public boolean isCanHeat() {
		return canHeat;
	}

	@JsonProperty("can_heat")
	public void setCanHeat(boolean canHeat) {
		this.canHeat = canHeat;
	}

	@JsonProperty("can_cool")
	public boolean isCanCool() {
		return canCool;
	}

	@JsonProperty("can_cool")
	public void setCanCool(boolean canCool) {
		this.canCool = canCool;
	}


}

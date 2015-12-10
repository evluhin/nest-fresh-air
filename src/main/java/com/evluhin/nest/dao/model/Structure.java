package com.evluhin.nest.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 */
@Entity
@Table
public class Structure extends NestEntity {


	@OneToMany
	private List<Alarm> alarms;
	@OneToMany
	private List<Thermostat> thermostats;

	@OneToMany
	private List<Pair> pairs;


	public List<Pair> getPairs() {
		return pairs;
	}

	public void setPairs(List<Pair> pairs) {
		this.pairs = pairs;
	}

	@JsonProperty("smoke_co_alarms")
	public List<Alarm> getAlarms() {
		return alarms;
	}

	@JsonProperty("smoke_co_alarms")
	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}

	public List<Thermostat> getThermostats() {
		return thermostats;
	}

	public void setThermostats(List<Thermostat> thermostats) {
		this.thermostats = thermostats;
	}

	@JsonProperty("structure_id")
	@Override
	public String getId() {
		return super.getId();
	}

	@JsonProperty("structure_id")
	@Override
	public void setId(String id) {
		super.setId(id);
	}
}

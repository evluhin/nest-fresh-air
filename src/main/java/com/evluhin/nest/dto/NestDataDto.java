package com.evluhin.nest.dto;

import com.evluhin.nest.dao.model.Alarm;
import com.evluhin.nest.dao.model.Structure;
import com.evluhin.nest.dao.model.Thermostat;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * DTO composite with updated values from Nest API.
 */
public class NestDataDto {

	private List<Thermostat> thermostats;
	private List<Alarm> alarms;
	private List<Structure> structures;

	public NestDataDto() {
	}

	public List<Thermostat> getThermostats() {
		return thermostats;
	}

	public void setThermostats(List<Thermostat> thermostats) {
		this.thermostats = thermostats;
	}

	public List<Alarm> getAlarms() {
		return alarms;
	}

	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}

	public List<Structure> getStructures() {
		return structures;
	}

	public void setStructures(List<Structure> structures) {
		this.structures = structures;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
						  .add("thermostats", thermostats)
						  .add("alarms", alarms)
						  .add("structures", structures)
						  .toString();
	}
}

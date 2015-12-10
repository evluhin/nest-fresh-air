package com.evluhin.nest.dto;

import com.evluhin.nest.dao.model.Alarm;
import com.evluhin.nest.dao.model.Structure;
import com.evluhin.nest.dao.model.Thermostat;

import java.util.List;

/**
 * @author sevluhin, on 10.12.15.
 */
public class Data {

	private List<Thermostat> thermostats;
	private List<Alarm> alarms;
	private List<Structure> structures;

	public Data() {
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
}

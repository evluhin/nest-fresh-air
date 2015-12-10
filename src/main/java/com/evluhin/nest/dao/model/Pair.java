package com.evluhin.nest.dao.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 */
@Entity
@Table(name = "pairs", uniqueConstraints = @UniqueConstraint(columnNames = {"alarm_id", "thermostat_id"}))
public class Pair {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "alarm_id", nullable = false)
	private Alarm alarm;
	@OneToOne
	@JoinColumn(name = "thermostat_id", nullable = false)
	private Thermostat thermostat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public Thermostat getThermostat() {
		return thermostat;
	}

	public void setThermostat(Thermostat thermostat) {
		this.thermostat = thermostat;
	}


}

package com.evluhin.nest.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 */
@Entity
@Table
public class Alarm extends Device {

	private String alarmState;

	public Alarm() {
	}

	public Alarm(String id) {
		super(id);
	}

	@JsonProperty("smoke_alarm_state")
	public String getAlarmState() {
		return alarmState;
	}

	@JsonProperty("smoke_alarm_state")
	public void setAlarmState(String alarmState) {
		this.alarmState = alarmState;
	}
}

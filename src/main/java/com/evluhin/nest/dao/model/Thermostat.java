package com.evluhin.nest.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 */
@Entity
@Table
public class Thermostat extends Device {

	public Thermostat() {
	}

	public Thermostat(String id) {
		super(id);
	}
}

package com.evluhin.nest.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 */
@Entity
@Table
public class Alarm extends Device {
	public Alarm() {
	}

	public Alarm(String id) {
		super(id);
	}
}

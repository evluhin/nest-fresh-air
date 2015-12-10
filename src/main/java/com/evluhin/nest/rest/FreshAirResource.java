package com.evluhin.nest.rest;

import com.evluhin.nest.FreshAirService;
import com.evluhin.nest.NestProperties;
import com.evluhin.nest.dao.model.Alarm;
import com.evluhin.nest.dao.model.Structure;
import com.evluhin.nest.dao.model.Thermostat;
import com.evluhin.nest.dto.Data;
import com.evluhin.nest.dto.SettingsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 */
@RestController
@RequestMapping("/api")
public class FreshAirResource {

	@Autowired
	private NestProperties nestProperties;

	@Autowired
	private FreshAirService service;

	@RequestMapping(value = "/data",method = RequestMethod.POST)
	public List<Structure> settings(@RequestBody Data data) {
		return service.update(data);
	}


	@RequestMapping(value = "/structures", method = RequestMethod.POST)
	public List<Structure> saveStructuresAndFind(@RequestBody List<Structure> structures) {
		List<Structure> returned = service.organizeStructures(structures);
		return returned;
	}

	@RequestMapping(value = "/alarms", method = RequestMethod.POST)
	public List<Alarm> saveAlarms(@RequestBody List<Alarm> alarms) {
		return service.saveAlarms(alarms);
	}

	@RequestMapping(value = "/thermostats", method = RequestMethod.POST)
	public List<Thermostat> saveThermostats(@RequestBody List<Thermostat> thermostats) {
		return service.saveThermostats(thermostats);
	}

}

package com.evluhin.nest.rest;

import com.evluhin.nest.FreshAirService;
import com.evluhin.nest.NestProperties;
import com.evluhin.nest.dao.model.Structure;
import com.evluhin.nest.dto.NestDataDto;
import com.evluhin.nest.dto.SettingsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	Logger log = LoggerFactory.getLogger(FreshAirResource.class);
	@Autowired
	private NestProperties nestProperties;

	@Autowired
	private FreshAirService service;


	@RequestMapping("/settings")
	public SettingsDto settings() {
		return new SettingsDto(nestProperties.getAuthUrl());
	}

	@RequestMapping(value = "/data", method = RequestMethod.POST)
	public List<Structure> settings(@RequestBody NestDataDto nestData) {
		List<Structure> structures = service.update(nestData);

		log.info("saved structures: {}", structures);
		return structures;
	}
}

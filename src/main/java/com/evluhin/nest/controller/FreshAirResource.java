package com.evluhin.nest.controller;

import com.evluhin.nest.NestProperties;
import com.evluhin.nest.dao.AlarmRepository;
import com.evluhin.nest.dao.PairRepository;
import com.evluhin.nest.dao.StructureRepository;
import com.evluhin.nest.dao.ThermostatRepository;
import com.evluhin.nest.dao.model.Alarm;
import com.evluhin.nest.dao.model.NestEntity;
import com.evluhin.nest.dao.model.Pair;
import com.evluhin.nest.dao.model.Structure;
import com.evluhin.nest.dao.model.Thermostat;
import com.evluhin.nest.dto.SettingsDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 */
@RestController
@RequestMapping("/api")
public class FreshAirResource {

	@Autowired
	NestProperties nestProperties;

	@Autowired
	StructureRepository structureRepository;
	@Autowired
	PairRepository pairRepository;

	@Autowired
	AlarmRepository alarmRepository;

	@Autowired
	ThermostatRepository thermostatRepository;

	@RequestMapping("/settings")
	public SettingsDto settings() {
		return new SettingsDto(nestProperties.getAuthUrl());
	}


	@RequestMapping("/structures")
	public List<Structure> structures() {
		return structureRepository.findAll();
	}

	@RequestMapping(value = "/structures", method = RequestMethod.POST)
	public List<Structure> saveStructuresAndFind(@RequestBody List<Structure> structures) {
		List<Structure> saved = new ArrayList<>();

		for (Structure structure : structures) {


			List<Alarm>
					savedAlarms =
					alarmRepository.findAll(structure.getAlarms().stream().map(NestEntity::getId).collect(Collectors.toList()));

			structure.setAlarms(savedAlarms);


			List<Thermostat>
					savedThermos =
					thermostatRepository.findAll(structure.getThermostats().stream().map(NestEntity::getId).collect(Collectors.toList()));
			structure.setThermostats(savedThermos);


			//find pairs
			if (CollectionUtils.isNotEmpty(savedAlarms) && CollectionUtils.isNotEmpty(savedThermos)) {

				List<Pair> pairs = new ArrayList<>();
				savedAlarms.stream().forEach(alarm -> {
					Optional<Thermostat> first = savedThermos.stream().filter(f -> f.getLocation().equals(alarm.getLocation()))
															 .findFirst();
					if (first.isPresent()) {
						Thermostat thermostat = first.get();

						Pair pair = new Pair();
						pair.setAlarm(alarm);
						pair.setThermostat(thermostat);
						//TODO find by location?
						Pair existingPair = pairRepository.findByAlarmIdAndThermostatId(alarm.getId(), thermostat.getId());
						if (existingPair == null) {
							pair.setStructure(structureRepository.findOne(structure.getId()));
							pairs.add(pair);
						} else {
							pairs.add(existingPair);
						}

					}
				});
				structure.setPairs(pairRepository.save(pairs));
			}

			Structure existing = structureRepository.findOne(structure.getId());
			if (existing != null) {
				existing.getPairs().clear();
				existing.getThermostats().clear();
				existing.getAlarms().clear();
				structureRepository.save(existing);
			}


			saved.add(structureRepository.save(structure));
		}

		return saved;
	}

	public Structure saveStructuresAndCreate(@RequestBody Structure structure) {


		List<Alarm> alarms = structure.getAlarms();
		//TODO check if empty
		List<Alarm> savedAlarms = alarmRepository.save(alarms);

		structure.setAlarms(savedAlarms);


		List<Thermostat> thermostats = structure.getThermostats();
		//TODO check if empty
		List<Thermostat> savedThermos = thermostatRepository.save(thermostats);
		structure.setThermostats(savedThermos);

		if (CollectionUtils.isNotEmpty(savedAlarms) && CollectionUtils.isNotEmpty(savedThermos)) {

			List<Pair> pairs = new ArrayList<>();
			savedAlarms.stream().forEach(a -> {
				Optional<Thermostat> first = thermostats.stream().filter(f -> f.getLocation().equals(a.getLocation())).findFirst();
				if (first.isPresent()) {

					Pair pair = new Pair();
					pair.setAlarm(a);
					pair.setThermostat(first.get());
					pair.setStructure(structure);
					pairs.add(pair);

				}
			});
			structure.setPairs(pairRepository.save(pairs));
		}


		Structure saved = structureRepository.save(structure);
		return saved;
	}


	@RequestMapping(value = "/alarms", method = RequestMethod.POST)
	public List<Alarm> saveAlarms(@RequestBody List<Alarm> alarms) {

		return alarmRepository.save(alarms);
	}

	@RequestMapping(value = "/thermostats", method = RequestMethod.POST)
	public List<Thermostat> saveThermostats(@RequestBody List<Thermostat> thermostats) {
		return thermostatRepository.save(thermostats);
	}

	@RequestMapping(value = "/pairs", method = RequestMethod.POST)
	public Pair savePair(@RequestBody Pair pair) {

		String alarmId = pair.getAlarm().getId();

		Alarm alarm = alarmRepository.findOne(alarmId);

		if (alarm == null) {
			throw new PairException("alarm not found");
		}


		String thermoId = pair.getThermostat().getId();
		Thermostat thermostat = thermostatRepository.findOne(thermoId);
		if (thermostat == null) {
			throw new PairException("thermostat not found");
		}


		String structureId = pair.getStructure().getId();
		Structure structure = structureRepository.findOne(structureId);
		if (structure == null) {
			throw new PairException("structure not found");
		}

		pair.setStructure(structure);

		//TODO: handle duplicate exception

		return pairRepository.save(pair);
	}


	@RequestMapping(value = "/pairs", method = RequestMethod.GET)
	public List<Pair> pairs(@RequestParam("structureId") String structureId) {

		Validate.notBlank(structureId);

		Structure one = structureRepository.findOne(structureId);

		if (one == null) {
			return Collections.emptyList();
		}

		return one.getPairs();
	}
	//+++++++ Exception handling

	@ExceptionHandler(FreshAirResource.PairException.class)
	@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
	@ResponseBody
	public String handlePairException(PairException e) {
		return e.getMessage();
	}

	private static class PairException extends RuntimeException {
		public PairException(String message) {
			super(message);
		}
	}
}

package com.evluhin.nest;

import com.evluhin.nest.dao.AlarmRepository;
import com.evluhin.nest.dao.PairRepository;
import com.evluhin.nest.dao.StructureRepository;
import com.evluhin.nest.dao.ThermostatRepository;
import com.evluhin.nest.dao.model.Alarm;
import com.evluhin.nest.dao.model.Device;
import com.evluhin.nest.dao.model.Pair;
import com.evluhin.nest.dao.model.Structure;
import com.evluhin.nest.dao.model.Thermostat;
import com.evluhin.nest.dto.NestDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sevluhin, on 10.12.15.
 */
@Service
public class FreshAirService {
	private Logger log = LoggerFactory.getLogger(FreshAirService.class);
	@Autowired
	private StructureRepository structureRepository;

	@Autowired
	private PairRepository pairRepository;

	@Autowired
	private AlarmRepository alarmRepository;

	@Autowired
	private ThermostatRepository thermostatRepository;

	public List<Structure> update(NestDataDto nestData) {
		log.info("new data received: {}", nestData);
		List<Structure> savedStructures = new ArrayList<>(nestData.getStructures().size());

		//just save all devices, even they don't have pairs
		List<Alarm> savedAlarms = alarmRepository.save(nestData.getAlarms());
		List<Thermostat> savedThermostats = thermostatRepository.save(nestData.getThermostats());

		List<Structure> structures = nestData.getStructures();
		for (Structure structure : structures) {

			//convert alarm dto to entities and filter
			List<Alarm> structureAlarms = findStructureDevice(savedAlarms, structure.getAlarms());
			structure.setAlarms(structureAlarms);

			//convert thermostat dto to entities and filter
			List<Thermostat> structureThermos = findStructureDevice(savedThermostats, structure.getThermostats());
			structure.setThermostats(structureThermos);

			List<Pair> pairs = findPairs(structure);
			log.info("pairs found: {}", pairs);
			structure.setPairs(pairs);

			Structure saved = structureRepository.save(structure);
			savedStructures.add(saved);
		}

		return savedStructures;
	}

	/**
	 * Find devices within whole list of devices that are belong to a structure.
	 *
	 * @param devices all list of devices
	 * @param structureDevices structure devices
	 * @param <T> alarm or thermostat object
	 * @return list of devices that belongs to a structure
	 */
	protected <T extends Device> List<T> findStructureDevice(List<T> devices, List<T> structureDevices) {
		return devices.stream().filter(a -> structureDevices.contains(a)).collect(Collectors.toList());
	}

	/**
	 * Find device pairs in a structure
	 *
	 * @param structure
	 * @return
	 */
	private List<Pair> findPairs(Structure structure) {
		List<Pair> pairs = new ArrayList<>();
		structure.getAlarms().stream().forEach(alarm -> {

			Optional<Thermostat>
					first =
					structure.getThermostats().stream().filter(f -> f.getLocation().equals(alarm.getLocation())).findFirst();
			//we use only one thermostat per location
			if (first.isPresent()) {
				Thermostat thermostat = first.get();

				Pair pair = new Pair();
				pair.setAlarm(alarm);
				pair.setThermostat(thermostat);
				Pair existingPair = pairRepository.findByAlarmIdAndThermostatId(alarm.getId(), thermostat.getId());
				if (existingPair == null) {

					pairs.add(pairRepository.save(pair));
				} else {
					pairs.add(existingPair);
				}

			}
		});
		return pairs;
	}
}

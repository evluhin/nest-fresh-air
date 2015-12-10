package com.evluhin.nest.dao;

import com.evluhin.nest.dao.model.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 */
@Repository
public interface PairRepository extends JpaRepository<Pair, UUID> {
	Pair findByAlarmIdAndThermostatId(String alarmId, String thermostatId);
}

package com.evluhin.nest.dao;

import com.evluhin.nest.dao.model.Thermostat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 */
@Repository
public interface ThermostatRepository extends JpaRepository<Thermostat, String> {
}

package com.evluhin.nest.dao;

import com.evluhin.nest.dao.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 */
@Repository
public interface AlarmRepository extends JpaRepository<Alarm, String> {
}

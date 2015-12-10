package com.evluhin.nest.dao;

import com.evluhin.nest.dao.model.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 */
@Repository
public interface StructureRepository extends JpaRepository<Structure, String> {
}

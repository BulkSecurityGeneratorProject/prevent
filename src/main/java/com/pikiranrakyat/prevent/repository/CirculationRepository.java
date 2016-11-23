package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.master.Circulation;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Circulation entity.
 */
@SuppressWarnings("unused")
public interface CirculationRepository extends JpaRepository<Circulation,Long> {

}

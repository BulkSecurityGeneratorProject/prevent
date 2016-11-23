package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.Locations;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Locations entity.
 */
@SuppressWarnings("unused")
public interface LocationsRepository extends JpaRepository<Locations,Long> {

}

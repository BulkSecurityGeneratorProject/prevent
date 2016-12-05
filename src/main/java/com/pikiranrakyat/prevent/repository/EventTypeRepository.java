package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.EventType;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;

/**
 * Spring Data JPA repository for the EventType entity.
 */
@SuppressWarnings("unused")
public interface EventTypeRepository extends JpaRepository<EventType, Long> {

    Optional<EventType> findByNameIgnoreCase(String name);
}

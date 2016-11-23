package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.master.EventType;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the EventType entity.
 */
@SuppressWarnings("unused")
public interface EventTypeRepository extends JpaRepository<EventType,Long> {

}

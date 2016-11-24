package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.Events;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Events entity.
 */
@SuppressWarnings("unused")
public interface EventsRepository extends JpaRepository<Events,Long> {

}

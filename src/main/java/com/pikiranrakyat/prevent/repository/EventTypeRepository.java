package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.EventType;

import com.pikiranrakyat.prevent.domain.Locations;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the EventType entity.
 */
@SuppressWarnings("unused")
public interface EventTypeRepository extends JpaRepository<EventType, Long> {

    Optional<EventType> findByNameIgnoreCase(String name);

    @Query("SELECT u FROM EventType u WHERE u.name LIKE CONCAT('%',:name,'%')")
    List<EventType> findEventTypeLikeName(@Param("name") String name);
}

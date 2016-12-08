package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Events entity.
 */
@SuppressWarnings("unused")
public interface EventsRepository extends JpaRepository<Events, Long> {

    Page<Events> findByAcceptIsTrue(Pageable pageable);

    Optional<Events> findByTitleIgnoreCase(String title);


}

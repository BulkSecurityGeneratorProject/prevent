package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.service.dto.EventOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Events.
 */
public interface EventsService {

    /**
     * Save a events.
     *
     * @param events the entity to save
     * @return the persisted entity
     */
    Events save(Events events);

    Events saveWithOrder(EventOrderDTO dto);

    Optional<Events> searchTitleIgnoreCase(String title);

    /**
     * Get all the events.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Events> findAll(Pageable pageable);

    Page<Events> findAllWhereAcceptIsTrue(Pageable pageable);

    /**
     * @param pageable
     * @return
     */
    Page<Events> findAllOrderByCreatedDateAsc(Pageable pageable);

    /**
     * Get the "id" events.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Events findOne(Long id);

    /**
     * Delete the "id" events.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the events corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Events> search(String query, Pageable pageable);

    Page<Events> searchWitAcceptIsTrue(String query, Pageable pageable);

}

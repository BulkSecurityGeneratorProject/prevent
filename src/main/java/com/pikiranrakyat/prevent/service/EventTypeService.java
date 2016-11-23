package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.master.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing EventType.
 */
public interface EventTypeService {

    /**
     * Save a eventType.
     *
     * @param eventType the entity to save
     * @return the persisted entity
     */
    EventType save(EventType eventType);

    /**
     *  Get all the eventTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EventType> findAll(Pageable pageable);

    /**
     *  Get the "id" eventType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EventType findOne(Long id);

    /**
     *  Delete the "id" eventType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the eventType corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EventType> search(String query, Pageable pageable);
}

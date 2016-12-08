package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.OrderRedaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing OrderRedaction.
 */
public interface OrderRedactionService {

    /**
     * Save a orderRedaction.
     *
     * @param orderRedaction the entity to save
     * @return the persisted entity
     */
    OrderRedaction save(OrderRedaction orderRedaction);

    /**
     * Get all the orderRedactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderRedaction> findAll(Pageable pageable);

    List<OrderRedaction> findByEvent(Long eventId);

    /**
     * Get the "id" orderRedaction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    OrderRedaction findOne(Long id);

    /**
     * Delete the "id" orderRedaction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the orderRedaction corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderRedaction> search(String query, Pageable pageable);
}

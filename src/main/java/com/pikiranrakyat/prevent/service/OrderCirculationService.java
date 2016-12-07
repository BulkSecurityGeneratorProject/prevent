package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.OrderCirculation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing OrderCirculation.
 */
public interface OrderCirculationService {

    /**
     * Save a orderCirculation.
     *
     * @param orderCirculation the entity to save
     * @return the persisted entity
     */
    OrderCirculation save(OrderCirculation orderCirculation);

    /**
     * Get all the orderCirculations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderCirculation> findAll(Pageable pageable);

    List<OrderCirculation> findByEvent(Long eventId);

    /**
     * Get the "id" orderCirculation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    OrderCirculation findOne(Long id);

    /**
     * Delete the "id" orderCirculation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the orderCirculation corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderCirculation> search(String query, Pageable pageable);
}

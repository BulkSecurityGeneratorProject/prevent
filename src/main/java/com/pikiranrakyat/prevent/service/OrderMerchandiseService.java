package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.OrderMerchandise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing OrderMerchandise.
 */
public interface OrderMerchandiseService {

    /**
     * Save a orderMerchandise.
     *
     * @param orderMerchandise the entity to save
     * @return the persisted entity
     */
    OrderMerchandise save(OrderMerchandise orderMerchandise);

    /**
     * Get all the orderMerchandises.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderMerchandise> findAll(Pageable pageable);

    List<OrderMerchandise> findAll();

    List<OrderMerchandise> findByEvent(Long eventId);

    /**
     * Get the "id" orderMerchandise.
     *
     * @param id the id of the entity
     * @return the entity
     */
    OrderMerchandise findOne(Long id);

    /**
     * Delete the "id" orderMerchandise.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the orderMerchandise corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderMerchandise> search(String query, Pageable pageable);
}

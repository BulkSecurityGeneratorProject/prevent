package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.Merchandise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Merchandise.
 */
public interface MerchandiseService {

    /**
     * Save a merchandise.
     *
     * @param merchandise the entity to save
     * @return the persisted entity
     */
    Merchandise save(Merchandise merchandise);

    /**
     * Get all the merchandises.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Merchandise> findAll(Pageable pageable);

    List<Merchandise> findAll();

    /**
     * Get the "id" merchandise.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Merchandise findOne(Long id);

    /**
     * Delete the "id" merchandise.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the merchandise corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Merchandise> search(String query, Pageable pageable);
}

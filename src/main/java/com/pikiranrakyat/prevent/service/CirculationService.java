package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.master.Circulation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Circulation.
 */
public interface CirculationService {

    /**
     * Save a circulation.
     *
     * @param circulation the entity to save
     * @return the persisted entity
     */
    Circulation save(Circulation circulation);

    /**
     *  Get all the circulations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Circulation> findAll(Pageable pageable);

    /**
     *  Get the "id" circulation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Circulation findOne(Long id);

    /**
     *  Delete the "id" circulation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the circulation corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Circulation> search(String query, Pageable pageable);
}

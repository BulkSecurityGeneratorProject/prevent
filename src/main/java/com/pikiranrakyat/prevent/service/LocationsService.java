package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.Locations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Locations.
 */
public interface LocationsService {

    /**
     * Save a locations.
     *
     * @param locations the entity to save
     * @return the persisted entity
     */
    Locations save(Locations locations);

    /**
     *  Get all the locations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Locations> findAll(Pageable pageable);

    /**
     *  Get the "id" locations.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Locations findOne(Long id);

    /**
     *  Delete the "id" locations.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the locations corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Locations> search(String query, Pageable pageable);
}

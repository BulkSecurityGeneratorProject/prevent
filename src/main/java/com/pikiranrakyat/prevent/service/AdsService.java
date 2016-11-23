package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.master.Ads;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Ads.
 */
public interface AdsService {

    /**
     * Save a ads.
     *
     * @param ads the entity to save
     * @return the persisted entity
     */
    Ads save(Ads ads);

    /**
     *  Get all the ads.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Ads> findAll(Pageable pageable);

    /**
     *  Get the "id" ads.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Ads findOne(Long id);

    /**
     *  Delete the "id" ads.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ads corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Ads> search(String query, Pageable pageable);
}

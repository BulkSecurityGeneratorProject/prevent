package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.AdsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing AdsCategory.
 */
public interface AdsCategoryService {

    /**
     * Save a adsCategory.
     *
     * @param adsCategory the entity to save
     * @return the persisted entity
     */
    AdsCategory save(AdsCategory adsCategory);

    /**
     *  Get all the adsCategories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AdsCategory> findAll(Pageable pageable);

    /**
     *  Get the "id" adsCategory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AdsCategory findOne(Long id);

    /**
     *  Delete the "id" adsCategory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the adsCategory corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AdsCategory> search(String query, Pageable pageable);
}

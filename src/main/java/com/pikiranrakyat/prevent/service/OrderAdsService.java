package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.OrderAds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing OrderAds.
 */
public interface OrderAdsService {

    /**
     * Save a orderAds.
     *
     * @param orderAds the entity to save
     * @return the persisted entity
     */
    OrderAds save(OrderAds orderAds);

    /**
     *  Get all the orderAds.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OrderAds> findAll(Pageable pageable);

    /**
     *  Get the "id" orderAds.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OrderAds findOne(Long id);

    /**
     *  Delete the "id" orderAds.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the orderAds corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OrderAds> search(String query, Pageable pageable);
}

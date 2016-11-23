package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.service.AdsCategoryService;
import com.pikiranrakyat.prevent.domain.AdsCategory;
import com.pikiranrakyat.prevent.repository.AdsCategoryRepository;
import com.pikiranrakyat.prevent.repository.search.AdsCategorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AdsCategory.
 */
@Service
@Transactional
public class AdsCategoryServiceImpl implements AdsCategoryService{

    private final Logger log = LoggerFactory.getLogger(AdsCategoryServiceImpl.class);
    
    @Inject
    private AdsCategoryRepository adsCategoryRepository;

    @Inject
    private AdsCategorySearchRepository adsCategorySearchRepository;

    /**
     * Save a adsCategory.
     *
     * @param adsCategory the entity to save
     * @return the persisted entity
     */
    public AdsCategory save(AdsCategory adsCategory) {
        log.debug("Request to save AdsCategory : {}", adsCategory);
        AdsCategory result = adsCategoryRepository.save(adsCategory);
        adsCategorySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the adsCategories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AdsCategory> findAll(Pageable pageable) {
        log.debug("Request to get all AdsCategories");
        Page<AdsCategory> result = adsCategoryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one adsCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AdsCategory findOne(Long id) {
        log.debug("Request to get AdsCategory : {}", id);
        AdsCategory adsCategory = adsCategoryRepository.findOne(id);
        return adsCategory;
    }

    /**
     *  Delete the  adsCategory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AdsCategory : {}", id);
        adsCategoryRepository.delete(id);
        adsCategorySearchRepository.delete(id);
    }

    /**
     * Search for the adsCategory corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AdsCategory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AdsCategories for query {}", query);
        Page<AdsCategory> result = adsCategorySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

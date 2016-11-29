package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.service.CirculationService;
import com.pikiranrakyat.prevent.domain.Circulation;
import com.pikiranrakyat.prevent.repository.CirculationRepository;
import com.pikiranrakyat.prevent.repository.search.CirculationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Circulation.
 */
@Service
@Transactional
public class CirculationServiceImpl implements CirculationService{

    private final Logger log = LoggerFactory.getLogger(CirculationServiceImpl.class);

    @Inject
    private CirculationRepository circulationRepository;

    @Inject
    private CirculationSearchRepository circulationSearchRepository;

    /**
     * Save a circulation.
     *
     * @param circulation the entity to save
     * @return the persisted entity
     */
    public Circulation save(Circulation circulation) {
        log.debug("Request to save Circulation : {}", circulation);
        Circulation result = circulationRepository.save(circulation);
        circulationSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the circulations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Circulation> findAll(Pageable pageable) {
        log.debug("Request to get all Circulations");
        Page<Circulation> result = circulationRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one circulation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Circulation findOne(Long id) {
        log.debug("Request to get Circulation : {}", id);
        Circulation circulation = circulationRepository.findOne(id);
        return circulation;
    }

    /**
     *  Delete the  circulation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Circulation : {}", id);
        circulationRepository.delete(id);
        circulationSearchRepository.delete(id);
    }

    /**
     * Search for the circulation corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Circulation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Circulations for query {}", query);
        Page<Circulation> result = circulationSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

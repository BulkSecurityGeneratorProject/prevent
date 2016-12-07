package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.service.RedactionService;
import com.pikiranrakyat.prevent.domain.Redaction;
import com.pikiranrakyat.prevent.repository.RedactionRepository;
import com.pikiranrakyat.prevent.repository.search.RedactionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Redaction.
 */
@Service
@Transactional
public class RedactionServiceImpl implements RedactionService{

    private final Logger log = LoggerFactory.getLogger(RedactionServiceImpl.class);

    @Inject
    private RedactionRepository redactionRepository;

    @Inject
    private RedactionSearchRepository redactionSearchRepository;

    /**
     * Save a redaction.
     *
     * @param redaction the entity to save
     * @return the persisted entity
     */
    public Redaction save(Redaction redaction) {
        log.debug("Request to save Redaction : {}", redaction);
        Redaction result = redactionRepository.save(redaction);
        redactionSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the redactions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Redaction> findAll(Pageable pageable) {
        log.debug("Request to get all Redactions");
        Page<Redaction> result = redactionRepository.findAll(pageable);
        return result;
    }

    @Override
    public List<Redaction> findAll() {
        log.debug("Request to get all Redactions");
        return redactionRepository.findAll();
    }

    /**
     *  Get one redaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Redaction findOne(Long id) {
        log.debug("Request to get Redaction : {}", id);
        Redaction redaction = redactionRepository.findOne(id);
        return redaction;
    }

    /**
     *  Delete the  redaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Redaction : {}", id);
        redactionRepository.delete(id);
        redactionSearchRepository.delete(id);
    }

    /**
     * Search for the redaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Redaction> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Redactions for query {}", query);
        Page<Redaction> result = redactionSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.service.MerchandiseService;
import com.pikiranrakyat.prevent.domain.Merchandise;
import com.pikiranrakyat.prevent.repository.MerchandiseRepository;
import com.pikiranrakyat.prevent.repository.search.MerchandiseSearchRepository;
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
 * Service Implementation for managing Merchandise.
 */
@Service
@Transactional
public class MerchandiseServiceImpl implements MerchandiseService{

    private final Logger log = LoggerFactory.getLogger(MerchandiseServiceImpl.class);

    @Inject
    private MerchandiseRepository merchandiseRepository;

    @Inject
    private MerchandiseSearchRepository merchandiseSearchRepository;

    /**
     * Save a merchandise.
     *
     * @param merchandise the entity to save
     * @return the persisted entity
     */
    public Merchandise save(Merchandise merchandise) {
        log.debug("Request to save Merchandise : {}", merchandise);
        Merchandise result = merchandiseRepository.save(merchandise);
        merchandiseSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the merchandises.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Merchandise> findAll(Pageable pageable) {
        log.debug("Request to get all Merchandises");
        Page<Merchandise> result = merchandiseRepository.findAll(pageable);
        return result;
    }

    @Override
    public List<Merchandise> findAll() {
        log.debug("Request to get all Merchandises");
        return merchandiseRepository.findAll();
    }

    /**
     *  Get one merchandise by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Merchandise findOne(Long id) {
        log.debug("Request to get Merchandise : {}", id);
        Merchandise merchandise = merchandiseRepository.findOne(id);
        return merchandise;
    }

    /**
     *  Delete the  merchandise by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Merchandise : {}", id);
        merchandiseRepository.delete(id);
        merchandiseSearchRepository.delete(id);
    }

    /**
     * Search for the merchandise corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Merchandise> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Merchandises for query {}", query);
        Page<Merchandise> result = merchandiseSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

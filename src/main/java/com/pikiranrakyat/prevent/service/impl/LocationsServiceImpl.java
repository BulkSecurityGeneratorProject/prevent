package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.service.LocationsService;
import com.pikiranrakyat.prevent.domain.Locations;
import com.pikiranrakyat.prevent.repository.LocationsRepository;
import com.pikiranrakyat.prevent.repository.search.LocationsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Locations.
 */
@Service
@Transactional
public class LocationsServiceImpl implements LocationsService {

    private final Logger log = LoggerFactory.getLogger(LocationsServiceImpl.class);

    @Inject
    private LocationsRepository locationsRepository;

    @Inject
    private LocationsSearchRepository locationsSearchRepository;

    /**
     * Save a locations.
     *
     * @param locations the entity to save
     * @return the persisted entity
     */
    public Locations save(Locations locations) {
        log.debug("Request to save Locations : {}", locations);
        Locations result = locationsRepository.save(locations);
        locationsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Locations> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationsRepository.findAll(pageable);
    }

    @Override
    public Optional<Locations> findByNameIgnoreCase(String name) {
        log.debug("Request find location by " + name);
        return locationsRepository.findByNameIgnoreCase(name);
    }

    /**
     * Get one locations by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Locations findOne(Long id) {
        log.debug("Request to get Locations : {}", id);
        Locations locations = locationsRepository.findOne(id);
        return locations;
    }

    /**
     * Delete the  locations by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Locations : {}", id);
        locationsRepository.delete(id);
        locationsSearchRepository.delete(id);
    }

    /**
     * Search for the locations corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Locations> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Locations for query {}", query);
        Page<Locations> result = locationsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

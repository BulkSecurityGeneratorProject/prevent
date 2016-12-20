package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.domain.Organizer;
import com.pikiranrakyat.prevent.domain.User;
import com.pikiranrakyat.prevent.repository.organizer.OrganizerRepository;
import com.pikiranrakyat.prevent.repository.search.OrganizerSearchRepository;
import com.pikiranrakyat.prevent.service.OrganizerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Organizer.
 */
@Service
@Transactional
public class OrganizerServiceImpl implements OrganizerService {

    private final Logger log = LoggerFactory.getLogger(OrganizerServiceImpl.class);

    @Inject
    private OrganizerRepository organizerRepository;

    @Inject
    private OrganizerSearchRepository organizerSearchRepository;

    /**
     * Save a organizer.
     *
     * @param organizer the entity to save
     * @return the persisted entity
     */
    public Organizer save(Organizer organizer) {
        log.debug("Request to save Organizer : {}", organizer);
        Organizer result = organizerRepository.save(organizer);
        organizerSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the organizers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Organizer> findAll(Pageable pageable) {
        log.debug("Request to get all Organizers");
        Page<Organizer> result = organizerRepository.findAll(pageable);
        return result;
    }

    @Override
    public Page<Organizer> findByCurrentUser(Pageable pageable) {
        return organizerRepository.findByUserIsCurrentUser(pageable);
    }

    @Override
    public List<Organizer> findByCurrentUser() {
        return organizerRepository.findByUserIsCurrentUser();
    }

    /**
     * Get one organizer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Organizer findOne(Long id) {
        log.debug("Request to get Organizer : {}", id);
        Organizer organizer = organizerRepository.findOne(id);
        return organizer;
    }

    @Override
    public Optional<Organizer> findByNameIgnoreCase(String name) {
        log.debug("Request to find organizer by name : " + name);
        return organizerRepository.findByNameIgnoreCase(name);
    }

    /**
     * Delete the  organizer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Organizer : {}", id);
        organizerRepository.delete(id);
        organizerSearchRepository.delete(id);
    }

    /**
     * Search for the organizer corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Organizer> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Organizers for query {}", query);
        return organizerSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public Page<Organizer> searchByUser(User user, String query, Pageable pageable) {
        log.debug("Request to search for a page of Organizers for query {}", query);
        return organizerSearchRepository.findByUser(user, queryStringQuery(query), pageable);
    }
}

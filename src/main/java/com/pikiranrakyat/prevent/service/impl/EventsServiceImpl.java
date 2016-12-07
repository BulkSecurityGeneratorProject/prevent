package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.repository.EventsRepository;
import com.pikiranrakyat.prevent.repository.search.EventsSearchRepository;
import com.pikiranrakyat.prevent.service.EventsService;
import com.pikiranrakyat.prevent.service.FileManagerService;
import com.pikiranrakyat.prevent.service.ImageManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Events.
 */
@Service
@Transactional
public class EventsServiceImpl implements EventsService {

    private final Logger log = LoggerFactory.getLogger(EventsServiceImpl.class);

    @Inject
    private EventsRepository eventsRepository;

    @Inject
    private EventsSearchRepository eventsSearchRepository;

    @Inject
    private FileManagerService fileManagerService;

    @Inject
    private ImageManagerService imageManagerService;

    /**
     * Save a events.
     *
     * @param events the entity to save
     * @return the persisted entity
     */
    public Events save(Events events) {
        log.debug("Request to save Events : {}", events);
        Events result = eventsRepository.save(events);
        eventsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the events.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Events> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        Page<Events> result = eventsRepository.findAll(pageable);
        return result;
    }

    @Override
    public Page<Events> findAllOrderByCreatedDateAsc(Pageable pageable) {
        log.debug("Request to get all Events order by created date");
        return eventsRepository.findAll(pageable);
    }

    /**
     * Get one events by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Events findOne(Long id) {
        log.debug("Request to get Events : {}", id);
        Events events = eventsRepository.findOne(id);
        return events;
    }

    /**
     * Delete the  events by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Events : {}", id);

        fileManagerService.delete(eventsRepository.findOne(id).getFile().getId());
        imageManagerService.delete(eventsRepository.findOne(id).getImage().getId());

        eventsRepository.delete(id);
        eventsSearchRepository.delete(id);
    }

    /**
     * Search for the events corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Events> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Events for query {}", query);
        Page<Events> result = eventsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

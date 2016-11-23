package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.service.EventTypeService;
import com.pikiranrakyat.prevent.domain.master.EventType;
import com.pikiranrakyat.prevent.repository.EventTypeRepository;
import com.pikiranrakyat.prevent.repository.search.EventTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EventType.
 */
@Service
@Transactional
public class EventTypeServiceImpl implements EventTypeService{

    private final Logger log = LoggerFactory.getLogger(EventTypeServiceImpl.class);

    @Inject
    private EventTypeRepository eventTypeRepository;

    @Inject
    private EventTypeSearchRepository eventTypeSearchRepository;

    /**
     * Save a eventType.
     *
     * @param eventType the entity to save
     * @return the persisted entity
     */
    public EventType save(EventType eventType) {
        log.debug("Request to save EventType : {}", eventType);
        EventType result = eventTypeRepository.save(eventType);
        eventTypeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the eventTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EventType> findAll(Pageable pageable) {
        log.debug("Request to get all EventTypes");
        Page<EventType> result = eventTypeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one eventType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EventType findOne(Long id) {
        log.debug("Request to get EventType : {}", id);
        EventType eventType = eventTypeRepository.findOne(id);
        return eventType;
    }

    /**
     *  Delete the  eventType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EventType : {}", id);
        eventTypeRepository.delete(id);
        eventTypeSearchRepository.delete(id);
    }

    /**
     * Search for the eventType corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EventType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EventTypes for query {}", query);
        Page<EventType> result = eventTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

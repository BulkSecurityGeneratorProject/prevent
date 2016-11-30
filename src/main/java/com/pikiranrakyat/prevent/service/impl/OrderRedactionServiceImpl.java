package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.service.OrderRedactionService;
import com.pikiranrakyat.prevent.domain.OrderRedaction;
import com.pikiranrakyat.prevent.repository.OrderRedactionRepository;
import com.pikiranrakyat.prevent.repository.search.OrderRedactionSearchRepository;
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
 * Service Implementation for managing OrderRedaction.
 */
@Service
@Transactional
public class OrderRedactionServiceImpl implements OrderRedactionService{

    private final Logger log = LoggerFactory.getLogger(OrderRedactionServiceImpl.class);
    
    @Inject
    private OrderRedactionRepository orderRedactionRepository;

    @Inject
    private OrderRedactionSearchRepository orderRedactionSearchRepository;

    /**
     * Save a orderRedaction.
     *
     * @param orderRedaction the entity to save
     * @return the persisted entity
     */
    public OrderRedaction save(OrderRedaction orderRedaction) {
        log.debug("Request to save OrderRedaction : {}", orderRedaction);
        OrderRedaction result = orderRedactionRepository.save(orderRedaction);
        orderRedactionSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the orderRedactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<OrderRedaction> findAll(Pageable pageable) {
        log.debug("Request to get all OrderRedactions");
        Page<OrderRedaction> result = orderRedactionRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one orderRedaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OrderRedaction findOne(Long id) {
        log.debug("Request to get OrderRedaction : {}", id);
        OrderRedaction orderRedaction = orderRedactionRepository.findOne(id);
        return orderRedaction;
    }

    /**
     *  Delete the  orderRedaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderRedaction : {}", id);
        orderRedactionRepository.delete(id);
        orderRedactionSearchRepository.delete(id);
    }

    /**
     * Search for the orderRedaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderRedaction> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderRedactions for query {}", query);
        Page<OrderRedaction> result = orderRedactionSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

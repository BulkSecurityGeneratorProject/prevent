package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.service.OrderMerchandiseService;
import com.pikiranrakyat.prevent.domain.OrderMerchandise;
import com.pikiranrakyat.prevent.repository.OrderMerchandiseRepository;
import com.pikiranrakyat.prevent.repository.search.OrderMerchandiseSearchRepository;
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
 * Service Implementation for managing OrderMerchandise.
 */
@Service
@Transactional
public class OrderMerchandiseServiceImpl implements OrderMerchandiseService{

    private final Logger log = LoggerFactory.getLogger(OrderMerchandiseServiceImpl.class);
    
    @Inject
    private OrderMerchandiseRepository orderMerchandiseRepository;

    @Inject
    private OrderMerchandiseSearchRepository orderMerchandiseSearchRepository;

    /**
     * Save a orderMerchandise.
     *
     * @param orderMerchandise the entity to save
     * @return the persisted entity
     */
    public OrderMerchandise save(OrderMerchandise orderMerchandise) {
        log.debug("Request to save OrderMerchandise : {}", orderMerchandise);
        OrderMerchandise result = orderMerchandiseRepository.save(orderMerchandise);
        orderMerchandiseSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the orderMerchandises.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<OrderMerchandise> findAll(Pageable pageable) {
        log.debug("Request to get all OrderMerchandises");
        Page<OrderMerchandise> result = orderMerchandiseRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one orderMerchandise by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OrderMerchandise findOne(Long id) {
        log.debug("Request to get OrderMerchandise : {}", id);
        OrderMerchandise orderMerchandise = orderMerchandiseRepository.findOne(id);
        return orderMerchandise;
    }

    /**
     *  Delete the  orderMerchandise by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderMerchandise : {}", id);
        orderMerchandiseRepository.delete(id);
        orderMerchandiseSearchRepository.delete(id);
    }

    /**
     * Search for the orderMerchandise corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderMerchandise> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderMerchandises for query {}", query);
        Page<OrderMerchandise> result = orderMerchandiseSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

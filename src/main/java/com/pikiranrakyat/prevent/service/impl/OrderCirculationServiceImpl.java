package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.domain.OrderCirculation;
import com.pikiranrakyat.prevent.repository.OrderCirculationRepository;
import com.pikiranrakyat.prevent.repository.search.OrderCirculationSearchRepository;
import com.pikiranrakyat.prevent.service.OrderCirculationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing OrderCirculation.
 */
@Service
@Transactional
public class OrderCirculationServiceImpl implements OrderCirculationService {

    private final Logger log = LoggerFactory.getLogger(OrderCirculationServiceImpl.class);

    @Inject
    private OrderCirculationRepository orderCirculationRepository;

    @Inject
    private OrderCirculationSearchRepository orderCirculationSearchRepository;

    /**
     * Save a orderCirculation.
     *
     * @param orderCirculation the entity to save
     * @return the persisted entity
     */
    public OrderCirculation save(OrderCirculation orderCirculation) {
        log.debug("Request to save OrderCirculation : {}", orderCirculation);
        OrderCirculation result = orderCirculationRepository.save(orderCirculation);
        orderCirculationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the orderCirculations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderCirculation> findAll(Pageable pageable) {
        log.debug("Request to get all OrderCirculations");
        Page<OrderCirculation> result = orderCirculationRepository.findAll(pageable);
        return result;
    }

    @Override
    public List<OrderCirculation> findByEvent(Long eventId) {
        return orderCirculationRepository.findByEventsId(eventId);
    }

    /**
     * Get one orderCirculation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrderCirculation findOne(Long id) {
        log.debug("Request to get OrderCirculation : {}", id);
        OrderCirculation orderCirculation = orderCirculationRepository.findOne(id);
        return orderCirculation;
    }

    /**
     * Delete the  orderCirculation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderCirculation : {}", id);
        orderCirculationRepository.delete(id);
        orderCirculationSearchRepository.delete(id);
    }

    /**
     * Search for the orderCirculation corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderCirculation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderCirculations for query {}", query);
        Page<OrderCirculation> result = orderCirculationSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

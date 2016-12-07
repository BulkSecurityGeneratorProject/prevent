package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.domain.OrderAds;
import com.pikiranrakyat.prevent.repository.OrderAdsRepository;
import com.pikiranrakyat.prevent.repository.search.OrderAdsSearchRepository;
import com.pikiranrakyat.prevent.service.OrderAdsService;
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
 * Service Implementation for managing OrderAds.
 */
@Service
@Transactional
public class OrderAdsServiceImpl implements OrderAdsService {

    private final Logger log = LoggerFactory.getLogger(OrderAdsServiceImpl.class);

    @Inject
    private OrderAdsRepository orderAdsRepository;

    @Inject
    private OrderAdsSearchRepository orderAdsSearchRepository;

    /**
     * Save a orderAds.
     *
     * @param orderAds the entity to save
     * @return the persisted entity
     */
    public OrderAds save(OrderAds orderAds) {
        log.debug("Request to save OrderAds : {}", orderAds);
        OrderAds result = orderAdsRepository.save(orderAds);
        orderAdsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the orderAds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderAds> findAll(Pageable pageable) {
        log.debug("Request to get all OrderAds");
        Page<OrderAds> result = orderAdsRepository.findAll(pageable);
        return result;
    }

    @Override
    public List<OrderAds> findByEvent(Long eventId) {
        return orderAdsRepository.findByEventsId(eventId);
    }

    /**
     * Get one orderAds by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrderAds findOne(Long id) {
        log.debug("Request to get OrderAds : {}", id);
        OrderAds orderAds = orderAdsRepository.findOne(id);
        return orderAds;
    }

    /**
     * Delete the  orderAds by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderAds : {}", id);
        orderAdsRepository.delete(id);
        orderAdsSearchRepository.delete(id);
    }

    /**
     * Search for the orderAds corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderAds> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderAds for query {}", query);
        Page<OrderAds> result = orderAdsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.service.AdsService;
import com.pikiranrakyat.prevent.domain.Ads;
import com.pikiranrakyat.prevent.repository.AdsRepository;
import com.pikiranrakyat.prevent.repository.search.AdsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ads.
 */
@Service
@Transactional
public class AdsServiceImpl implements AdsService {

    private final Logger log = LoggerFactory.getLogger(AdsServiceImpl.class);

    @Inject
    private AdsRepository adsRepository;

    @Inject
    private AdsSearchRepository adsSearchRepository;

    /**
     * Save a ads.
     *
     * @param ads the entity to save
     * @return the persisted entity
     */
    public Ads save(Ads ads) {
        log.debug("Request to save Ads : {}", ads);
        ads.setTotalPrice(BigDecimal.valueOf(ads.getAdsCategory().getPrice().doubleValue() * ads.getCols() * ads.getMillimeter()));
        Ads result = adsRepository.save(ads);
        adsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ads.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ads> findAll(Pageable pageable) {
        log.debug("Request to get all Ads");
        Page<Ads> result = adsRepository.findAll(pageable);
        return result;
    }

    @Override
    public List<Ads> findAll() {
        log.debug("Request to get all Ads");
        return adsRepository.findAll();
    }

    /**
     * Get one ads by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Ads findOne(Long id) {
        log.debug("Request to get Ads : {}", id);
        Ads ads = adsRepository.findOne(id);
        return ads;
    }

    /**
     * Delete the  ads by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ads : {}", id);
        adsRepository.delete(id);
        adsSearchRepository.delete(id);
    }

    /**
     * Search for the ads corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ads> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ads for query {}", query);
        Page<Ads> result = adsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

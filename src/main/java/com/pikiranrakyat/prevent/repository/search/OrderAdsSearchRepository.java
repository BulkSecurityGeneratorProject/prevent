package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.OrderAds;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderAds entity.
 */
public interface OrderAdsSearchRepository extends ElasticsearchRepository<OrderAds, Long> {
}

package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.OrderCirculation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderCirculation entity.
 */
public interface OrderCirculationSearchRepository extends ElasticsearchRepository<OrderCirculation, Long> {
}

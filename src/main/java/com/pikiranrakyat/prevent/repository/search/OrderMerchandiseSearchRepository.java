package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.OrderMerchandise;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderMerchandise entity.
 */
public interface OrderMerchandiseSearchRepository extends ElasticsearchRepository<OrderMerchandise, Long> {
}

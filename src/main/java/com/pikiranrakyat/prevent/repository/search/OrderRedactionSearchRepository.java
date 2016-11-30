package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.OrderRedaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderRedaction entity.
 */
public interface OrderRedactionSearchRepository extends ElasticsearchRepository<OrderRedaction, Long> {
}

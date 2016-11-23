package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.master.EventType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EventType entity.
 */
public interface EventTypeSearchRepository extends ElasticsearchRepository<EventType, Long> {
}

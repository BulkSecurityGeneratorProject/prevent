package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Events;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Events entity.
 */
public interface EventsSearchRepository extends ElasticsearchRepository<Events, Long> {
}

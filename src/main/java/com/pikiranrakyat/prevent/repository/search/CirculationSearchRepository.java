package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Circulation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Circulation entity.
 */
public interface CirculationSearchRepository extends ElasticsearchRepository<Circulation, Long> {
}

package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Locations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Locations entity.
 */
public interface LocationsSearchRepository extends ElasticsearchRepository<Locations, Long> {
}

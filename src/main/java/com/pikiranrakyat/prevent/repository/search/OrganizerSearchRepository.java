package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Organizer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Organizer entity.
 */
public interface OrganizerSearchRepository extends ElasticsearchRepository<Organizer, Long> {
}

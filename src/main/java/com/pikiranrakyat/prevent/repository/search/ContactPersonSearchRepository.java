package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.ContactPerson;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ContactPerson entity.
 */
public interface ContactPersonSearchRepository extends ElasticsearchRepository<ContactPerson, Long> {
}

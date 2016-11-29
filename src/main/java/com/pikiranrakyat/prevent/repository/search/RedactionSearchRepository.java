package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Redaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Redaction entity.
 */
public interface RedactionSearchRepository extends ElasticsearchRepository<Redaction, Long> {
}

package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Merchandise;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Merchandise entity.
 */
public interface MerchandiseSearchRepository extends ElasticsearchRepository<Merchandise, Long> {
}

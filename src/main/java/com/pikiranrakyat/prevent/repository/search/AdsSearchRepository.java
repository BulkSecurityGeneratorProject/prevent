package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Ads;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ads entity.
 */
public interface AdsSearchRepository extends ElasticsearchRepository<Ads, Long> {
}

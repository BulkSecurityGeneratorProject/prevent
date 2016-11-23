package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.AdsCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AdsCategory entity.
 */
public interface AdsCategorySearchRepository extends ElasticsearchRepository<AdsCategory, Long> {
}

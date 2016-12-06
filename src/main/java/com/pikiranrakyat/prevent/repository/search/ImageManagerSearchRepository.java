package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.ImageManager;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FileManager entity.
 */
public interface ImageManagerSearchRepository extends ElasticsearchRepository<ImageManager, Long> {
}

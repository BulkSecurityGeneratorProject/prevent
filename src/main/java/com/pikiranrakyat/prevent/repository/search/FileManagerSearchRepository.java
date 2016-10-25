package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.FileManager;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FileManager entity.
 */
public interface FileManagerSearchRepository extends ElasticsearchRepository<FileManager, Long> {
}

package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Organizer;
import com.pikiranrakyat.prevent.domain.User;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Organizer entity.
 */
public interface OrganizerSearchRepository extends ElasticsearchRepository<Organizer, Long> {

    Page<Organizer> findByUser(User user, QueryStringQueryBuilder query, Pageable pageable);

}

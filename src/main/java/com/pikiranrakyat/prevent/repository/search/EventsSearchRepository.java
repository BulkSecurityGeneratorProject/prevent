package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Events;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Events entity.
 */
public interface EventsSearchRepository extends ElasticsearchRepository<Events, Long> {

    Page<Events> findByAcceptIsTrue(QueryStringQueryBuilder query, Pageable pageable);

}

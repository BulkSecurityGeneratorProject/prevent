package com.pikiranrakyat.prevent.repository.search;

import com.pikiranrakyat.prevent.domain.Events;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;

/**
 * Spring Data ElasticSearch repository for the Events entity.
 */
public interface EventsSearchRepository extends ElasticsearchRepository<Events, Long> {

    Page<Events> findByAcceptIsTrue(QueryStringQueryBuilder query, Pageable pageable);

    Page<Events> findByStartsBetween(LocalDateTime starts, LocalDateTime ends, Pageable pageable);

    Page<Events> findByStartsBetweenAndTitleAllIgnoreCase(LocalDateTime starts, LocalDateTime ends, String title, Pageable pageable);
    Page<Events> findByStartsBetweenOrTitleLike(LocalDateTime starts, LocalDateTime ends, String title, Pageable pageable);

    Page<Events> findByTitleAllIgnoreCase(String title, Pageable pageable);


}

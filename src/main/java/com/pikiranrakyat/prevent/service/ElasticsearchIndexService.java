package com.pikiranrakyat.prevent.service;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.*;
import com.pikiranrakyat.prevent.repository.*;
import com.pikiranrakyat.prevent.repository.organizer.OrganizerRepository;
import com.pikiranrakyat.prevent.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    @Inject
    private AdsRepository adsRepository;

    @Inject
    private AdsSearchRepository adsSearchRepository;

    @Inject
    private AdsCategoryRepository adsCategoryRepository;

    @Inject
    private AdsCategorySearchRepository adsCategorySearchRepository;

    @Inject
    private CirculationRepository circulationRepository;

    @Inject
    private CirculationSearchRepository circulationSearchRepository;

    @Inject
    private ContactPersonRepository contactPersonRepository;

    @Inject
    private ContactPersonSearchRepository contactPersonSearchRepository;

    @Inject
    private EventTypeRepository eventTypeRepository;

    @Inject
    private EventTypeSearchRepository eventTypeSearchRepository;

    @Inject
    private EventsRepository eventsRepository;

    @Inject
    private EventsSearchRepository eventsSearchRepository;

    @Inject
    private FileManagerRepository fileManagerRepository;

    @Inject
    private FileManagerSearchRepository fileManagerSearchRepository;

    @Inject
    private LocationsRepository locationsRepository;

    @Inject
    private LocationsSearchRepository locationsSearchRepository;

    @Inject
    private MerchandiseRepository merchandiseRepository;

    @Inject
    private MerchandiseSearchRepository merchandiseSearchRepository;

    @Inject
    private OrderAdsRepository orderAdsRepository;

    @Inject
    private OrderAdsSearchRepository orderAdsSearchRepository;

    @Inject
    private OrderCirculationRepository orderCirculationRepository;

    @Inject
    private OrderCirculationSearchRepository orderCirculationSearchRepository;

    @Inject
    private OrderMerchandiseRepository orderMerchandiseRepository;

    @Inject
    private OrderMerchandiseSearchRepository orderMerchandiseSearchRepository;

    @Inject
    private OrderRedactionRepository orderRedactionRepository;

    @Inject
    private OrderRedactionSearchRepository orderRedactionSearchRepository;

    @Inject
    private OrganizerRepository organizerRepository;

    @Inject
    private OrganizerSearchRepository organizerSearchRepository;

    @Inject
    private RedactionRepository redactionRepository;

    @Inject
    private RedactionSearchRepository redactionSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    @Timed
    public void reindexAll() {
        reindexForClass(Ads.class, adsRepository, adsSearchRepository);
        reindexForClass(AdsCategory.class, adsCategoryRepository, adsCategorySearchRepository);
        reindexForClass(Circulation.class, circulationRepository, circulationSearchRepository);
        reindexForClass(ContactPerson.class, contactPersonRepository, contactPersonSearchRepository);
        reindexForClass(EventType.class, eventTypeRepository, eventTypeSearchRepository);
        reindexForClass(Events.class, eventsRepository, eventsSearchRepository);
        reindexForClass(FileManager.class, fileManagerRepository, fileManagerSearchRepository);
        reindexForClass(Locations.class, locationsRepository, locationsSearchRepository);
        reindexForClass(Merchandise.class, merchandiseRepository, merchandiseSearchRepository);
        reindexForClass(OrderAds.class, orderAdsRepository, orderAdsSearchRepository);
        reindexForClass(OrderCirculation.class, orderCirculationRepository, orderCirculationSearchRepository);
        reindexForClass(OrderMerchandise.class, orderMerchandiseRepository, orderMerchandiseSearchRepository);
        reindexForClass(OrderRedaction.class, orderRedactionRepository, orderRedactionSearchRepository);
        reindexForClass(Organizer.class, organizerRepository, organizerSearchRepository);
        reindexForClass(Redaction.class, redactionRepository, redactionSearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    private <T> void reindexForClass(Class<T> entityClass, JpaRepository<T, Long> jpaRepository,
                                                          ElasticsearchRepository<T, Long> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}

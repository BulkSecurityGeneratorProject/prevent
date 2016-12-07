package com.pikiranrakyat.prevent.service.impl;

import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.domain.OrderAds;
import com.pikiranrakyat.prevent.domain.OrderCirculation;
import com.pikiranrakyat.prevent.domain.OrderMerchandise;
import com.pikiranrakyat.prevent.repository.EventsRepository;
import com.pikiranrakyat.prevent.repository.search.EventsSearchRepository;
import com.pikiranrakyat.prevent.service.*;
import com.pikiranrakyat.prevent.service.dto.EventOrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Events.
 */
@Service
@Transactional
public class EventsServiceImpl implements EventsService {

    private final Logger log = LoggerFactory.getLogger(EventsServiceImpl.class);

    @Inject
    private EventsRepository eventsRepository;

    @Inject
    private EventsSearchRepository eventsSearchRepository;

    @Inject
    private FileManagerService fileManagerService;

    @Inject
    private ImageManagerService imageManagerService;

    @Inject
    private OrderMerchandiseService orderMerchandiseService;

    @Inject
    private OrderCirculationService orderCirculationService;

    @Inject
    private OrderAdsService orderAdsService;

    /**
     * Save a events.
     *
     * @param events the entity to save
     * @return the persisted entity
     */
    public Events save(Events events) {
        log.debug("Request to save Events : {}", events);
        Events result = eventsRepository.save(events);
        eventsSearchRepository.save(result);
        return result;
    }

    @Override
    public Events saveWithOrder(EventOrderDTO dto) {
        Events events = new Events();

        if (dto.getId() != null)
            events.setId(dto.getId());

        events.setTitle(dto.getTitle());
        events.setDescription(dto.getDescription());
        events.setStarts(dto.getStarts());
        events.setEnds(dto.getEnds());
        events.setLocationName(dto.getLocationName());
        events.setLocationAddress(dto.getLocationAddress());
        events.setLocationLatitude(dto.getLocationLatitude());
        events.setLocationLongitude(dto.getLocationLongitude());
        events.setSubtotal(dto.getSubtotal());
        events.setAgreeDate(dto.getAgreeDate());
        events.setAccept(dto.getAccept());
        events.setNote(dto.getNote());
        events.setIsOrder(dto.getOrder());
        events.setEventType(dto.getEventType());
        events.setOrganizer(dto.getOrganizer());
        events.setImage(dto.getImage());
        events.setFile(dto.getFile());
        Events save = save(events);

        if (dto.getOrderMerchandises().size() > 0)
            dto.getOrderMerchandises()
                .forEach(merchandiseDTO -> {

                    OrderMerchandise orderMerchandise = new OrderMerchandise();

                    if (merchandiseDTO.getId() != null)
                        orderMerchandise.setId(merchandiseDTO.getId());
                    if (merchandiseDTO.getOrderNumber() != null)
                        orderMerchandise.setOrderNumber(merchandiseDTO.getOrderNumber());

                    orderMerchandise.setOrderNumber(UUID.randomUUID().toString());
                    orderMerchandise.setAccept(merchandiseDTO.getAccept());
                    orderMerchandise.setNote(merchandiseDTO.getNote());
                    orderMerchandise.setQty(merchandiseDTO.getQty());
                    orderMerchandise.setTotal(merchandiseDTO.getTotal());
                    orderMerchandise.setMerchandise(merchandiseDTO.getMerchandise());
                    orderMerchandise.setEvents(save);

                    orderMerchandiseService.save(orderMerchandise);
                });


        if (dto.getOrderCirculations().size() > 0)
            dto.getOrderCirculations()
                .forEach(o -> {
                    OrderCirculation orderCirculation = new OrderCirculation();

                    if (o.getId() != null)
                        orderCirculation.setId(o.getId());

                    if (o.getOrderNumber() != null)
                        orderCirculation.setOrderNumber(o.getOrderNumber());

                    orderCirculation.setOrderNumber(UUID.randomUUID().toString());
                    orderCirculation.setAccept(o.getAccept());
                    orderCirculation.setNote(o.getNote());
                    orderCirculation.setQty(o.getQty());
                    orderCirculation.setTotal(o.getTotal());
                    orderCirculation.setCirculation(o.getCirculation());
                    orderCirculation.setEvents(save);

                    orderCirculationService.save(orderCirculation);
                });

        if (dto.getOrderAds().size() > 0)
            dto.getOrderAds()
                .forEach(o -> {
                    OrderAds orderAds = new OrderAds();

                    if (o.getId() != null)
                        orderAds.setId(o.getId());

                    if (o.getOrderNumber() != null)
                        orderAds.setOrderNumber(o.getOrderNumber());

                    orderAds.setOrderNumber(UUID.randomUUID().toString());
                    orderAds.setTitle(o.getTitle());
                    orderAds.setContent(o.getContent());
                    orderAds.setNote(o.getNote());
                    orderAds.setAccept(o.getAccept());
                    orderAds.setPublishDate(o.getPublishDate());
                    orderAds.setTotal(o.getTotal());
                    orderAds.setAds(o.getAds());
                    orderAds.setEvents(save);

                    orderAdsService.save(orderAds);

                });

        return save;
    }

    /**
     * Get all the events.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Events> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        Page<Events> result = eventsRepository.findAll(pageable);
        return result;
    }

    @Override
    public Page<Events> findAllOrderByCreatedDateAsc(Pageable pageable) {
        log.debug("Request to get all Events order by created date");
        return eventsRepository.findAll(pageable);
    }

    /**
     * Get one events by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Events findOne(Long id) {
        log.debug("Request to get Events : {}", id);
        Events events = eventsRepository.findOne(id);
        return events;
    }

    /**
     * Delete the  events by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Events : {}", id);

        fileManagerService.delete(eventsRepository.findOne(id).getFile().getId());
        imageManagerService.delete(eventsRepository.findOne(id).getImage().getId());

        eventsRepository.delete(id);
        eventsSearchRepository.delete(id);
    }

    /**
     * Search for the events corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Events> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Events for query {}", query);
        Page<Events> result = eventsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}

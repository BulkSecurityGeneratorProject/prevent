package com.pikiranrakyat.prevent.web.rest.user;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.repository.EventsRepository;
import com.pikiranrakyat.prevent.security.SecurityUtils;
import com.pikiranrakyat.prevent.service.*;
import com.pikiranrakyat.prevent.service.dto.EventOrderDTO;
import com.pikiranrakyat.prevent.web.rest.util.HeaderUtil;
import com.pikiranrakyat.prevent.web.rest.util.PaginationUtil;
import com.pikiranrakyat.prevent.web.rest.vm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Events.
 */
@RestController
@RequestMapping("/api/user")
public class UserEventsResource {

    private final Logger log = LoggerFactory.getLogger(UserEventsResource.class);

    @Inject
    private EventsService eventsService;

    @Inject
    private OrderMerchandiseService orderMerchandiseService;

    @Inject
    private OrderCirculationService orderCirculationService;

    @Inject
    private OrderAdsService orderAdsService;

    @Inject
    private OrderRedactionService orderRedactionService;

    @Inject
    private EventsRepository eventsRepository;

    /**
     * POST  /events : Create a new events.
     *
     * @param events the events to create
     * @return the ResponseEntity with status 201 (Created) and with body the new events, or with status 400 (Bad Request) if the events has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/events",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Events> createEvents(@Valid @RequestBody EventOrderDTO events) throws URISyntaxException {
        log.debug("REST request to save Events : {}", events);
        if (events.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("events", "idexists", "A new events cannot already have an ID")).body(null);
        }

        if (events.getImage() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("events", "image not exists ", " Mohon masukan brosur jpg")).body(null);
        } else if (events.getFile() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("events", "file not exists ", "Mohon masukan file pdf brosur")).body(null);
        } else {
            Events result = eventsService.saveWithOrder(events);
            return ResponseEntity.created(new URI("/api/events/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("events", result.getId().toString()))
                .body(result);
        }


    }

    /**
     * PUT  /events : Updates an existing events.
     *
     * @param events the events to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated events,
     * or with status 400 (Bad Request) if the events is not valid,
     * or with status 500 (Internal Server Error) if the events couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/events",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Events> updateEvents(@Valid @RequestBody EventOrderDTO events) throws URISyntaxException {
        log.debug("REST request to update Events : {}", events);
        if (events.getId() == null) {
            return createEvents(events);
        }

        if (events.getImage() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("events", "image not exists ", " Mohon masukan brosur jpg")).body(null);
        } else if (events.getFile() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("events", "file not exists ", "Mohon masukan file pdf brosur")).body(null);
        } else {
            Events result = eventsService.saveWithOrder(events);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("events", events.getId().toString()))
                .body(result);
        }

    }

    /**
     * GET  /events : get all the events.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of events in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedEventsVM>> getAllEvents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Events");

        Page<Events> page = eventsRepository.findByCreatedBy(SecurityUtils.getCurrentUserLogin(), pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user/events");
        return new ResponseEntity<>(page.getContent().stream().map(ManagedEventsVM::new).collect(Collectors.toList()), headers, HttpStatus.OK);
    }

    /**
     * GET  /events/:id : get the "id" events.
     *
     * @param id the id of the events to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the events, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/events/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventOrderDTO> getEvents(@PathVariable Long id) {
        log.debug("REST request to get Events : {}", id);
        Events events = eventsService.findOne(id);
        return Optional.ofNullable(events)
            .map(result -> {
                EventOrderDTO dto = new EventOrderDTO(result);

                dto.setOrderMerchandises(
                    orderMerchandiseService
                        .findByEvent(dto.getId())
                        .stream()
                        .map(ManagedOrderMerchandiseVM::new)
                        .collect(Collectors.toList()));

                dto.setOrderCirculations(
                    orderCirculationService.findByEvent(dto.getId())
                        .stream()
                        .map(ManagedOrderCirculationVM::new)
                        .collect(Collectors.toList())
                );

                dto.setOrderAds(
                    orderAdsService.findByEvent(dto.getId())
                        .stream()
                        .map(ManagedOrderAdsVM::new)
                        .collect(Collectors.toList())
                );

                dto.setOrderRedactions(
                    orderRedactionService.findByEvent(dto.getId())
                        .stream()
                        .map(ManagedOrderRedactionVM::new)
                        .collect(Collectors.toList())
                );


                return new ResponseEntity<>(dto, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * DELETE  /events/:id : delete the "id" events.
     *
     * @param id the id of the events to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/events/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEvents(@PathVariable Long id) {
        log.debug("REST request to delete Events : {}", id);
        eventsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("events", id.toString())).build();
    }


    /**
     * SEARCH  /_search/events?query=:query : search for the events corresponding
     * to the query.
     *
     * @param query    the query of the events search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Events>> searchEvents(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Events for query {}", query);
        Page<Events> page = eventsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

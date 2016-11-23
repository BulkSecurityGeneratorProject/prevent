package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.EventType;
import com.pikiranrakyat.prevent.service.EventTypeService;
import com.pikiranrakyat.prevent.web.rest.util.HeaderUtil;
import com.pikiranrakyat.prevent.web.rest.util.PaginationUtil;
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

/**
 * REST controller for managing EventType.
 */
@RestController
@RequestMapping("/api")
public class EventTypeResource {

    private final Logger log = LoggerFactory.getLogger(EventTypeResource.class);

    @Inject
    private EventTypeService eventTypeService;

    /**
     * POST  /event-types : Create a new eventType.
     *
     * @param eventType the eventType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eventType, or with status 400 (Bad Request) if the eventType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/event-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventType> createEventType(@Valid @RequestBody EventType eventType) throws URISyntaxException {
        log.debug("REST request to save EventType : {}", eventType);
        if (eventType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("eventType", "idexists", "A new eventType cannot already have an ID")).body(null);
        }
        EventType result = eventTypeService.save(eventType);
        return ResponseEntity.created(new URI("/api/event-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("eventType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /event-types : Updates an existing eventType.
     *
     * @param eventType the eventType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated eventType,
     * or with status 400 (Bad Request) if the eventType is not valid,
     * or with status 500 (Internal Server Error) if the eventType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/event-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventType> updateEventType(@Valid @RequestBody EventType eventType) throws URISyntaxException {
        log.debug("REST request to update EventType : {}", eventType);
        if (eventType.getId() == null) {
            return createEventType(eventType);
        }
        EventType result = eventTypeService.save(eventType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("eventType", eventType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /event-types : get all the eventTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of eventTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/event-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EventType>> getAllEventTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EventTypes");
        Page<EventType> page = eventTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/event-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /event-types/:id : get the "id" eventType.
     *
     * @param id the id of the eventType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eventType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/event-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventType> getEventType(@PathVariable Long id) {
        log.debug("REST request to get EventType : {}", id);
        EventType eventType = eventTypeService.findOne(id);
        return Optional.ofNullable(eventType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /event-types/:id : delete the "id" eventType.
     *
     * @param id the id of the eventType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/event-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEventType(@PathVariable Long id) {
        log.debug("REST request to delete EventType : {}", id);
        eventTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("eventType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/event-types?query=:query : search for the eventType corresponding
     * to the query.
     *
     * @param query the query of the eventType search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/event-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EventType>> searchEventTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of EventTypes for query {}", query);
        Page<EventType> page = eventTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/event-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

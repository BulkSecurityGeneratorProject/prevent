package com.pikiranrakyat.prevent.web.rest.manage;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.service.EventsService;
import com.pikiranrakyat.prevent.web.rest.util.PaginationUtil;
import com.pikiranrakyat.prevent.web.rest.vm.ManagedEventsVM;
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Events.
 */
@RestController
@RequestMapping("/api")
public class ManageEventsResource {

    private final Logger log = LoggerFactory.getLogger(ManageEventsResource.class);

    @Inject
    private EventsService eventsService;


    /**
     * GET  /manage/events : get all the events.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of events in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/manage/events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedEventsVM>> getAllEvents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Events");
        Page<Events> page = eventsService.findAllOrderByCreatedDateAsc(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/manage/events");
        return new ResponseEntity<>(page.getContent().stream().map(ManagedEventsVM::new).collect(Collectors.toList()), headers, HttpStatus.OK);
    }

    /**
     * GET  /manage/events/:id : get the "id" events.
     *
     * @param id the id of the events to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the events, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/manage/events/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Events> getEvents(@PathVariable Long id) {
        log.debug("REST request to get Events : {}", id);
        Events events = eventsService.findOne(id);
        return Optional.ofNullable(events)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * SEARCH  /_search/manage/events?query=:query : search for the events corresponding
     * to the query.
     *
     * @param query    the query of the events search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/manage/events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Events>> searchEvents(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Events for query {}", query);
        Page<Events> page = eventsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/manage/events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

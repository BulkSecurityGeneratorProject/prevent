package com.pikiranrakyat.prevent.web.rest.manage;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.service.EventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.ZonedDateTime;

/**
 * REST controller for managing Events.
 */
@RestController
@RequestMapping("/api/order")
public class OrderEventsResource {

    private final Logger log = LoggerFactory.getLogger(OrderEventsResource.class);

    @Inject
    private EventsService eventsService;

    @RequestMapping(value = "/event/{id}/accept",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Events acceptEvent(@PathVariable Long id) {
        log.debug("REST request to get Events : {}", id);
        Events events = eventsService.findOne(id);
        events.setAccept(true);
        events.setAgreeDate(ZonedDateTime.now());
        return eventsService.save(events);
    }

    @RequestMapping(value = "/event/{id}/reject",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Events rejectEvent(@PathVariable Long id) {
        log.debug("REST request to get Events : {}", id);
        Events events = eventsService.findOne(id);
        events.setAccept(false);
        return eventsService.save(events);
    }


}

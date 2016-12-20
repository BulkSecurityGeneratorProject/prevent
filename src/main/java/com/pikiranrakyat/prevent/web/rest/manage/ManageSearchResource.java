package com.pikiranrakyat.prevent.web.rest.manage;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.EventType;
import com.pikiranrakyat.prevent.domain.Organizer;
import com.pikiranrakyat.prevent.domain.User;
import com.pikiranrakyat.prevent.repository.EventTypeRepository;
import com.pikiranrakyat.prevent.repository.UserRepository;
import com.pikiranrakyat.prevent.repository.organizer.OrganizerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing Locations.
 */
@RestController
@RequestMapping("/api/list")
public class ManageSearchResource {

    private final Logger log = LoggerFactory.getLogger(ManageSearchResource.class);


    @Inject
    private EventTypeRepository eventTypeRepository;

    @Inject
    private UserRepository userRepository;


    @Inject
    private OrganizerRepository organizerRepository;


//    /**
//     * GET  /event-type: get all the event type.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of event type in body
//     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
//     */
//    @RequestMapping(value = "/event-type",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public List<EventType> findEventTypeLikeName(@RequestParam(value = "name", required = true) String name)
//        throws URISyntaxException {
//        log.debug("REST request to find event type by name" + name);
//        return eventTypeRepository.findEventTypeLikeName(name).stream().limit(5).collect(Collectors.toList());
//    }

    /**
     * GET  /event-type: get all the event type.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of event type in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/event-type",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EventType> findEventTypeAll()
        throws URISyntaxException {
        log.debug("REST request to find event type all");
        return eventTypeRepository.findAll()
            .stream()
            .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
            .collect(Collectors.toList());
    }


//    @Inject
//    private LocationsRepository locationsRepository;
//
//    @Inject
//    private LocationsService locationsService;
//
//    /**
//     * GET  /location/search: get all the locations.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of locations in body
//     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
//     */
//    @RequestMapping(value = "/location/search",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public List<Locations> findLocation(@RequestParam(value = "name", required = true) String name)
//        throws URISyntaxException {
//        log.debug("REST request to find location by name" + name);
//        return locationsRepository.findLocationLikeName(name)
//            .stream()
//            .limit(10)
//            .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
//            .collect(Collectors.toList());
//    }


//    /**
//     * GET  /location/search: get all the locations.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of locations in body
//     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
//     */
//    @RequestMapping(value = "/location/check",
//        method = RequestMethod.POST,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public ResponseEntity<?> checkLocation(@RequestBody Locations locations)
//        throws URISyntaxException {
//        log.debug("REST request to check location by " + locations);
//
//        Locations locationCheck = locationsService
//            .findByNameIgnoreCase(locations.getName())
//            .orElseGet(() -> {
//                Locations location = new Locations();
//                location.setName(locations.getName());
//                location.setAddress(locations.getAddress());
//                location.setCity(locations.getCity());
//                location.setState(locations.getState());
//                location.setPostalCode(locations.getPostalCode());
//                location.setLatitude(locations.getLatitude());
//                location.setLongitude(locations.getLongitude());
//                return locationsService.save(location);
//            });
//        return new ResponseEntity<>(locationCheck, HttpStatus.OK);
//    }


//    /**
//     * GET  /organizer: get all the organizer.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of organizer in body
//     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
//     */
//    @RequestMapping(value = "/organizer",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public List<Organizer> findOrganizerLikeNameAndWhereUserLogin(@RequestParam(value = "name", required = true) String name)
//        throws URISyntaxException {
//        log.debug("REST request to find organizer by name" + name);
//        return organizerRepository.findOrganizerLikeNameWhereUserLogin(name).stream().limit(5).collect(Collectors.toList());
//    }

    /**
     * GET  /organizer: get all the event type.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of event type in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/organizer",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Organizer> findOrganizerAll()
        throws URISyntaxException {
        log.debug("REST request to find event type all");
        return organizerRepository.findAll()
            .stream()
            .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
            .collect(Collectors.toList());
    }


    /**
     * GET  /user: get all the user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of event type in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/user",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getAllUser()
        throws URISyntaxException {
        log.debug("REST request to user list");
        return userRepository.findAll();
    }


}

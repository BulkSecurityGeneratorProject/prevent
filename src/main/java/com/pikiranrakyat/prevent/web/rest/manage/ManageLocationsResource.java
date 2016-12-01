package com.pikiranrakyat.prevent.web.rest.manage;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.service.LocationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;

/**
 * REST controller for managing Locations.
 */
@RestController
@RequestMapping("/api")
public class ManageLocationsResource {

    private final Logger log = LoggerFactory.getLogger(ManageLocationsResource.class);

    @Inject
    private LocationsService locationsService;


    /**
     * GET  /location/search: get all the locations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of locations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/location/search",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> findLocation(
        @RequestParam(value = "name", required = true) String name
    )
        throws URISyntaxException {
        log.debug("REST request to find location by name");

        return locationsService.findByNameIgnoreCase(name)
            .map(locations -> new ResponseEntity<>(locations, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}

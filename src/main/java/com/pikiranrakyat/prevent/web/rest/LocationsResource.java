package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.Locations;
import com.pikiranrakyat.prevent.service.LocationsService;
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
 * REST controller for managing Locations.
 */
@RestController
@RequestMapping("/api")
public class LocationsResource {

    private final Logger log = LoggerFactory.getLogger(LocationsResource.class);

    @Inject
    private LocationsService locationsService;

    /**
     * POST  /locations : Create a new locations.
     *
     * @param locations the locations to create
     * @return the ResponseEntity with status 201 (Created) and with body the new locations, or with status 400 (Bad Request) if the locations has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/locations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Locations> createLocations(@Valid @RequestBody Locations locations) throws URISyntaxException {
        log.debug("REST request to save Locations : {}", locations);
        if (locations.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("locations", "idexists", "A new locations cannot already have an ID")).body(null);
        }
        Locations result = locationsService.save(locations);
        return ResponseEntity.created(new URI("/api/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("locations", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /locations : Updates an existing locations.
     *
     * @param locations the locations to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated locations,
     * or with status 400 (Bad Request) if the locations is not valid,
     * or with status 500 (Internal Server Error) if the locations couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/locations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Locations> updateLocations(@Valid @RequestBody Locations locations) throws URISyntaxException {
        log.debug("REST request to update Locations : {}", locations);
        if (locations.getId() == null) {
            return createLocations(locations);
        }
        Locations result = locationsService.save(locations);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("locations", locations.getId().toString()))
            .body(result);
    }

    /**
     * GET  /locations : get all the locations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of locations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/locations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Locations>> getAllLocations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Locations");
        Page<Locations> page = locationsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/locations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /locations/:id : get the "id" locations.
     *
     * @param id the id of the locations to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the locations, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/locations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Locations> getLocations(@PathVariable Long id) {
        log.debug("REST request to get Locations : {}", id);
        Locations locations = locationsService.findOne(id);
        return Optional.ofNullable(locations)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /locations/:id : delete the "id" locations.
     *
     * @param id the id of the locations to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/locations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLocations(@PathVariable Long id) {
        log.debug("REST request to delete Locations : {}", id);
        locationsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("locations", id.toString())).build();
    }

    /**
     * SEARCH  /_search/locations?query=:query : search for the locations corresponding
     * to the query.
     *
     * @param query the query of the locations search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/locations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Locations>> searchLocations(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Locations for query {}", query);
        Page<Locations> page = locationsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/locations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

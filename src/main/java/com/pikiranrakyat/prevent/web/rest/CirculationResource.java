package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.master.Circulation;
import com.pikiranrakyat.prevent.service.CirculationService;
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
 * REST controller for managing Circulation.
 */
@RestController
@RequestMapping("/api")
public class CirculationResource {

    private final Logger log = LoggerFactory.getLogger(CirculationResource.class);

    @Inject
    private CirculationService circulationService;

    /**
     * POST  /circulations : Create a new circulation.
     *
     * @param circulation the circulation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new circulation, or with status 400 (Bad Request) if the circulation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/circulations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Circulation> createCirculation(@Valid @RequestBody Circulation circulation) throws URISyntaxException {
        log.debug("REST request to save Circulation : {}", circulation);
        if (circulation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("circulation", "idexists", "A new circulation cannot already have an ID")).body(null);
        }
        Circulation result = circulationService.save(circulation);
        return ResponseEntity.created(new URI("/api/circulations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("circulation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /circulations : Updates an existing circulation.
     *
     * @param circulation the circulation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated circulation,
     * or with status 400 (Bad Request) if the circulation is not valid,
     * or with status 500 (Internal Server Error) if the circulation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/circulations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Circulation> updateCirculation(@Valid @RequestBody Circulation circulation) throws URISyntaxException {
        log.debug("REST request to update Circulation : {}", circulation);
        if (circulation.getId() == null) {
            return createCirculation(circulation);
        }
        Circulation result = circulationService.save(circulation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("circulation", circulation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /circulations : get all the circulations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of circulations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/circulations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Circulation>> getAllCirculations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Circulations");
        Page<Circulation> page = circulationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/circulations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /circulations/:id : get the "id" circulation.
     *
     * @param id the id of the circulation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the circulation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/circulations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Circulation> getCirculation(@PathVariable Long id) {
        log.debug("REST request to get Circulation : {}", id);
        Circulation circulation = circulationService.findOne(id);
        return Optional.ofNullable(circulation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /circulations/:id : delete the "id" circulation.
     *
     * @param id the id of the circulation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/circulations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCirculation(@PathVariable Long id) {
        log.debug("REST request to delete Circulation : {}", id);
        circulationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("circulation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/circulations?query=:query : search for the circulation corresponding
     * to the query.
     *
     * @param query the query of the circulation search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/circulations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Circulation>> searchCirculations(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Circulations for query {}", query);
        Page<Circulation> page = circulationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/circulations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

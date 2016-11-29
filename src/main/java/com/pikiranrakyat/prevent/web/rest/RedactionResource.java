package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.Redaction;
import com.pikiranrakyat.prevent.service.RedactionService;
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
 * REST controller for managing Redaction.
 */
@RestController
@RequestMapping("/api")
public class RedactionResource {

    private final Logger log = LoggerFactory.getLogger(RedactionResource.class);

    @Inject
    private RedactionService redactionService;

    /**
     * POST  /redactions : Create a new redaction.
     *
     * @param redaction the redaction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new redaction, or with status 400 (Bad Request) if the redaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/redactions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Redaction> createRedaction(@Valid @RequestBody Redaction redaction) throws URISyntaxException {
        log.debug("REST request to save Redaction : {}", redaction);
        if (redaction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("redaction", "idexists", "A new redaction cannot already have an ID")).body(null);
        }
        Redaction result = redactionService.save(redaction);
        return ResponseEntity.created(new URI("/api/redactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("redaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /redactions : Updates an existing redaction.
     *
     * @param redaction the redaction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated redaction,
     * or with status 400 (Bad Request) if the redaction is not valid,
     * or with status 500 (Internal Server Error) if the redaction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/redactions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Redaction> updateRedaction(@Valid @RequestBody Redaction redaction) throws URISyntaxException {
        log.debug("REST request to update Redaction : {}", redaction);
        if (redaction.getId() == null) {
            return createRedaction(redaction);
        }
        Redaction result = redactionService.save(redaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("redaction", redaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /redactions : get all the redactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of redactions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/redactions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Redaction>> getAllRedactions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Redactions");
        Page<Redaction> page = redactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/redactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /redactions/:id : get the "id" redaction.
     *
     * @param id the id of the redaction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the redaction, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/redactions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Redaction> getRedaction(@PathVariable Long id) {
        log.debug("REST request to get Redaction : {}", id);
        Redaction redaction = redactionService.findOne(id);
        return Optional.ofNullable(redaction)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /redactions/:id : delete the "id" redaction.
     *
     * @param id the id of the redaction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/redactions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRedaction(@PathVariable Long id) {
        log.debug("REST request to delete Redaction : {}", id);
        redactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("redaction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/redactions?query=:query : search for the redaction corresponding
     * to the query.
     *
     * @param query the query of the redaction search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/redactions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Redaction>> searchRedactions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Redactions for query {}", query);
        Page<Redaction> page = redactionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/redactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

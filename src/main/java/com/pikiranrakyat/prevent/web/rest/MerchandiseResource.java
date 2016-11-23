package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.master.Merchandise;
import com.pikiranrakyat.prevent.service.MerchandiseService;
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
 * REST controller for managing Merchandise.
 */
@RestController
@RequestMapping("/api")
public class MerchandiseResource {

    private final Logger log = LoggerFactory.getLogger(MerchandiseResource.class);

    @Inject
    private MerchandiseService merchandiseService;

    /**
     * POST  /merchandises : Create a new merchandise.
     *
     * @param merchandise the merchandise to create
     * @return the ResponseEntity with status 201 (Created) and with body the new merchandise, or with status 400 (Bad Request) if the merchandise has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/merchandises",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Merchandise> createMerchandise(@Valid @RequestBody Merchandise merchandise) throws URISyntaxException {
        log.debug("REST request to save Merchandise : {}", merchandise);
        if (merchandise.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("merchandise", "idexists", "A new merchandise cannot already have an ID")).body(null);
        }
        Merchandise result = merchandiseService.save(merchandise);
        return ResponseEntity.created(new URI("/api/merchandises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("merchandise", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /merchandises : Updates an existing merchandise.
     *
     * @param merchandise the merchandise to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated merchandise,
     * or with status 400 (Bad Request) if the merchandise is not valid,
     * or with status 500 (Internal Server Error) if the merchandise couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/merchandises",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Merchandise> updateMerchandise(@Valid @RequestBody Merchandise merchandise) throws URISyntaxException {
        log.debug("REST request to update Merchandise : {}", merchandise);
        if (merchandise.getId() == null) {
            return createMerchandise(merchandise);
        }
        Merchandise result = merchandiseService.save(merchandise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("merchandise", merchandise.getId().toString()))
            .body(result);
    }

    /**
     * GET  /merchandises : get all the merchandises.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of merchandises in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/merchandises",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Merchandise>> getAllMerchandises(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Merchandises");
        Page<Merchandise> page = merchandiseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/merchandises");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /merchandises/:id : get the "id" merchandise.
     *
     * @param id the id of the merchandise to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the merchandise, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/merchandises/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Merchandise> getMerchandise(@PathVariable Long id) {
        log.debug("REST request to get Merchandise : {}", id);
        Merchandise merchandise = merchandiseService.findOne(id);
        return Optional.ofNullable(merchandise)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /merchandises/:id : delete the "id" merchandise.
     *
     * @param id the id of the merchandise to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/merchandises/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMerchandise(@PathVariable Long id) {
        log.debug("REST request to delete Merchandise : {}", id);
        merchandiseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("merchandise", id.toString())).build();
    }

    /**
     * SEARCH  /_search/merchandises?query=:query : search for the merchandise corresponding
     * to the query.
     *
     * @param query the query of the merchandise search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/merchandises",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Merchandise>> searchMerchandises(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Merchandises for query {}", query);
        Page<Merchandise> page = merchandiseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/merchandises");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

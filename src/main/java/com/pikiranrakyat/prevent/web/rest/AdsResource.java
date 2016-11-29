package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.Ads;
import com.pikiranrakyat.prevent.service.AdsService;
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
 * REST controller for managing Ads.
 */
@RestController
@RequestMapping("/api")
public class AdsResource {

    private final Logger log = LoggerFactory.getLogger(AdsResource.class);

    @Inject
    private AdsService adsService;

    /**
     * POST  /ads : Create a new ads.
     *
     * @param ads the ads to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ads, or with status 400 (Bad Request) if the ads has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ads",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ads> createAds(@Valid @RequestBody Ads ads) throws URISyntaxException {
        log.debug("REST request to save Ads : {}", ads);
        if (ads.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ads", "idexists", "A new ads cannot already have an ID")).body(null);
        }


        Ads result = adsService.save(ads);
        return ResponseEntity.created(new URI("/api/ads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ads", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ads : Updates an existing ads.
     *
     * @param ads the ads to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ads,
     * or with status 400 (Bad Request) if the ads is not valid,
     * or with status 500 (Internal Server Error) if the ads couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ads",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ads> updateAds(@Valid @RequestBody Ads ads) throws URISyntaxException {
        log.debug("REST request to update Ads : {}", ads);
        if (ads.getId() == null) {
            return createAds(ads);
        }
        Ads result = adsService.save(ads);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ads", ads.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ads : get all the ads.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ads in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ads>> getAllAds(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ads");
        Page<Ads> page = adsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ads/:id : get the "id" ads.
     *
     * @param id the id of the ads to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ads, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ads/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ads> getAds(@PathVariable Long id) {
        log.debug("REST request to get Ads : {}", id);
        Ads ads = adsService.findOne(id);
        return Optional.ofNullable(ads)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ads/:id : delete the "id" ads.
     *
     * @param id the id of the ads to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ads/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAds(@PathVariable Long id) {
        log.debug("REST request to delete Ads : {}", id);
        adsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ads", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ads?query=:query : search for the ads corresponding
     * to the query.
     *
     * @param query    the query of the ads search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/ads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ads>> searchAds(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Ads for query {}", query);
        Page<Ads> page = adsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

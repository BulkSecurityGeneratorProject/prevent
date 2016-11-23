package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.AdsCategory;
import com.pikiranrakyat.prevent.service.AdsCategoryService;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AdsCategory.
 */
@RestController
@RequestMapping("/api")
public class AdsCategoryResource {

    private final Logger log = LoggerFactory.getLogger(AdsCategoryResource.class);
        
    @Inject
    private AdsCategoryService adsCategoryService;

    /**
     * POST  /ads-categories : Create a new adsCategory.
     *
     * @param adsCategory the adsCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adsCategory, or with status 400 (Bad Request) if the adsCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ads-categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdsCategory> createAdsCategory(@Valid @RequestBody AdsCategory adsCategory) throws URISyntaxException {
        log.debug("REST request to save AdsCategory : {}", adsCategory);
        if (adsCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adsCategory", "idexists", "A new adsCategory cannot already have an ID")).body(null);
        }
        AdsCategory result = adsCategoryService.save(adsCategory);
        return ResponseEntity.created(new URI("/api/ads-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("adsCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ads-categories : Updates an existing adsCategory.
     *
     * @param adsCategory the adsCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adsCategory,
     * or with status 400 (Bad Request) if the adsCategory is not valid,
     * or with status 500 (Internal Server Error) if the adsCategory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ads-categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdsCategory> updateAdsCategory(@Valid @RequestBody AdsCategory adsCategory) throws URISyntaxException {
        log.debug("REST request to update AdsCategory : {}", adsCategory);
        if (adsCategory.getId() == null) {
            return createAdsCategory(adsCategory);
        }
        AdsCategory result = adsCategoryService.save(adsCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("adsCategory", adsCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ads-categories : get all the adsCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of adsCategories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ads-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AdsCategory>> getAllAdsCategories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AdsCategories");
        Page<AdsCategory> page = adsCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ads-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ads-categories/:id : get the "id" adsCategory.
     *
     * @param id the id of the adsCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adsCategory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ads-categories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdsCategory> getAdsCategory(@PathVariable Long id) {
        log.debug("REST request to get AdsCategory : {}", id);
        AdsCategory adsCategory = adsCategoryService.findOne(id);
        return Optional.ofNullable(adsCategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ads-categories/:id : delete the "id" adsCategory.
     *
     * @param id the id of the adsCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ads-categories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdsCategory(@PathVariable Long id) {
        log.debug("REST request to delete AdsCategory : {}", id);
        adsCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("adsCategory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ads-categories?query=:query : search for the adsCategory corresponding
     * to the query.
     *
     * @param query the query of the adsCategory search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/ads-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AdsCategory>> searchAdsCategories(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AdsCategories for query {}", query);
        Page<AdsCategory> page = adsCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ads-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

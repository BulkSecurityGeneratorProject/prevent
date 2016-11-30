package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.OrderRedaction;
import com.pikiranrakyat.prevent.service.OrderRedactionService;
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
import java.util.UUID;

/**
 * REST controller for managing OrderRedaction.
 */
@RestController
@RequestMapping("/api")
public class OrderRedactionResource {

    private final Logger log = LoggerFactory.getLogger(OrderRedactionResource.class);

    @Inject
    private OrderRedactionService orderRedactionService;

    /**
     * POST  /order-redactions : Create a new orderRedaction.
     *
     * @param orderRedaction the orderRedaction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderRedaction, or with status 400 (Bad Request) if the orderRedaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-redactions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderRedaction> createOrderRedaction(@Valid @RequestBody OrderRedaction orderRedaction) throws URISyntaxException {
        log.debug("REST request to save OrderRedaction : {}", orderRedaction);
        if (orderRedaction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderRedaction", "idexists", "A new orderRedaction cannot already have an ID")).body(null);
        }
        orderRedaction.setOrderNumber(UUID.randomUUID().toString());
        OrderRedaction result = orderRedactionService.save(orderRedaction);
        return ResponseEntity.created(new URI("/api/order-redactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderRedaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-redactions : Updates an existing orderRedaction.
     *
     * @param orderRedaction the orderRedaction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderRedaction,
     * or with status 400 (Bad Request) if the orderRedaction is not valid,
     * or with status 500 (Internal Server Error) if the orderRedaction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-redactions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderRedaction> updateOrderRedaction(@Valid @RequestBody OrderRedaction orderRedaction) throws URISyntaxException {
        log.debug("REST request to update OrderRedaction : {}", orderRedaction);
        if (orderRedaction.getId() == null) {
            return createOrderRedaction(orderRedaction);
        }
        OrderRedaction result = orderRedactionService.save(orderRedaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderRedaction", orderRedaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-redactions : get all the orderRedactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderRedactions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/order-redactions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<OrderRedaction>> getAllOrderRedactions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OrderRedactions");
        Page<OrderRedaction> page = orderRedactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-redactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-redactions/:id : get the "id" orderRedaction.
     *
     * @param id the id of the orderRedaction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderRedaction, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/order-redactions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderRedaction> getOrderRedaction(@PathVariable Long id) {
        log.debug("REST request to get OrderRedaction : {}", id);
        OrderRedaction orderRedaction = orderRedactionService.findOne(id);
        return Optional.ofNullable(orderRedaction)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-redactions/:id : delete the "id" orderRedaction.
     *
     * @param id the id of the orderRedaction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/order-redactions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderRedaction(@PathVariable Long id) {
        log.debug("REST request to delete OrderRedaction : {}", id);
        orderRedactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderRedaction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-redactions?query=:query : search for the orderRedaction corresponding
     * to the query.
     *
     * @param query    the query of the orderRedaction search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/order-redactions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<OrderRedaction>> searchOrderRedactions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OrderRedactions for query {}", query);
        Page<OrderRedaction> page = orderRedactionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-redactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

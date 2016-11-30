package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.OrderCirculation;
import com.pikiranrakyat.prevent.service.OrderCirculationService;
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
 * REST controller for managing OrderCirculation.
 */
@RestController
@RequestMapping("/api")
public class OrderCirculationResource {

    private final Logger log = LoggerFactory.getLogger(OrderCirculationResource.class);

    @Inject
    private OrderCirculationService orderCirculationService;

    /**
     * POST  /order-circulations : Create a new orderCirculation.
     *
     * @param orderCirculation the orderCirculation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderCirculation, or with status 400 (Bad Request) if the orderCirculation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-circulations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderCirculation> createOrderCirculation(@Valid @RequestBody OrderCirculation orderCirculation) throws URISyntaxException {
        log.debug("REST request to save OrderCirculation : {}", orderCirculation);
        if (orderCirculation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderCirculation", "idexists", "A new orderCirculation cannot already have an ID")).body(null);
        }
        orderCirculation.setOrderNumber(UUID.randomUUID().toString());
        OrderCirculation result = orderCirculationService.save(orderCirculation);
        return ResponseEntity.created(new URI("/api/order-circulations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderCirculation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-circulations : Updates an existing orderCirculation.
     *
     * @param orderCirculation the orderCirculation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderCirculation,
     * or with status 400 (Bad Request) if the orderCirculation is not valid,
     * or with status 500 (Internal Server Error) if the orderCirculation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-circulations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderCirculation> updateOrderCirculation(@Valid @RequestBody OrderCirculation orderCirculation) throws URISyntaxException {
        log.debug("REST request to update OrderCirculation : {}", orderCirculation);
        if (orderCirculation.getId() == null) {
            return createOrderCirculation(orderCirculation);
        }
        OrderCirculation result = orderCirculationService.save(orderCirculation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderCirculation", orderCirculation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-circulations : get all the orderCirculations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderCirculations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/order-circulations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<OrderCirculation>> getAllOrderCirculations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OrderCirculations");
        Page<OrderCirculation> page = orderCirculationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-circulations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-circulations/:id : get the "id" orderCirculation.
     *
     * @param id the id of the orderCirculation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderCirculation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/order-circulations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderCirculation> getOrderCirculation(@PathVariable Long id) {
        log.debug("REST request to get OrderCirculation : {}", id);
        OrderCirculation orderCirculation = orderCirculationService.findOne(id);
        return Optional.ofNullable(orderCirculation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-circulations/:id : delete the "id" orderCirculation.
     *
     * @param id the id of the orderCirculation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/order-circulations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderCirculation(@PathVariable Long id) {
        log.debug("REST request to delete OrderCirculation : {}", id);
        orderCirculationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderCirculation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-circulations?query=:query : search for the orderCirculation corresponding
     * to the query.
     *
     * @param query    the query of the orderCirculation search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/order-circulations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<OrderCirculation>> searchOrderCirculations(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OrderCirculations for query {}", query);
        Page<OrderCirculation> page = orderCirculationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-circulations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
